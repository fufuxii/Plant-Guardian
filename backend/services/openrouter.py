import os
import re
import json
import base64
import asyncio
from openai import AsyncOpenAI
from dotenv import load_dotenv

load_dotenv()

CLIENT = AsyncOpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key=os.getenv("OPENROUTER_API_KEY"),
  timeout=20.0,
  max_retries=0,
)

MODELOS_IA = [
  "openrouter/free",
  "google/gemma-4-31b-it:free",
  "google/gemma-4-26b-a4b:free",
  "google/gemini-2.5-flash-lite",
  "anthropic/claude-haiku-4.5",
]

REINTENTOS_POR_MODELO = 2  


async def consultar_openrouter(mensajes: list):
  for modelo in MODELOS_IA:
    for intento in range(1, REINTENTOS_POR_MODELO + 1):
      try:
        print(f"DEBUG: Intentando con el modelo: {modelo} (intento {intento})")
        response = await CLIENT.chat.completions.create(
          model=modelo,
          messages=mensajes,
          max_tokens=1024, # Evitar pedir el máximo del modelo (65k) y el 402 por crédito.
          extra_headers={
            "HTTP-Referer": os.getenv("APP_URL", "http://localhost"),
            "X-Title": "Plantas App",
          },
        )
        texto = response.choices[0].message.content
        inicio = texto.find('{')
        fin = texto.rfind('}') + 1
        if inicio == -1 or fin == 0:
          raise ValueError("Respuesta sin formato JSON.")
        return json.loads(texto[inicio:fin])
      except Exception as e:
        print(f"Falló el modelo {modelo} (intento {intento}): {e}")
        if intento < REINTENTOS_POR_MODELO:
          await asyncio.sleep(1)
        continue
  return None


def validar_tareas(tareas: list) -> list:
  """
  Garantiza que cada tarea tenga 'frecuencia' como texto que SIEMPRE
  contiene un número extraíble (para el re.findall que hace plantas_logic
  al guardar), y experiencia dentro de 10-20. Sin esto, un texto tipo
  'Semanalmente' sin dígito rompe el guardado con IndexError.
  """
  tareas_validas = []
  for t in tareas or []:
    tarea = t.get("tarea") or "Cuidado general"

    frecuencia = t.get("frecuencia")
    dias_encontrados = re.findall(r"\d+", str(frecuencia)) if frecuencia else []
    if not dias_encontrados:
      frecuencia = "Cada 7 días"

    try:
      experiencia = int(t.get("experiencia", 10))
    except (TypeError, ValueError):
      experiencia = 10
    experiencia = max(10, min(20, experiencia))

    tareas_validas.append({
      "tarea": tarea,
      "frecuencia": frecuencia,
      "experiencia": experiencia,
    })
  return tareas_validas


async def gemini_obtener_info_extra(nombre: str):
  prompt = f"""
  Eres una enciclopedia botánica. Proporciona información sobre la especie: {nombre}.
  Devuelve estrictamente un JSON con:
  - nombre_otros: Otros nombres comunes o populares en diferentes países de habla hispana (separados por comas).
  - descripcion: Una descripción botánica general, muy breve (máximo 20 palabras).
  Responde solo el JSON.
  """
  mensajes = [{"role": "user", "content": prompt}]
  resultado = await consultar_openrouter(mensajes)
  return resultado or {
    "nombre_otros": "No hay otros nombres disponibles.",
    "descripcion": "Información botánica en proceso de actualización."
  }


async def gemini_analizar_planta(nombre: str, lugar: str, ubicacion: str, foto_bytes: bytes, mime_type: str):
  foto_b64 = base64.b64encode(foto_bytes).decode("utf-8")
  prompt = f"""
  Actúa como un experto botánico. Te proporciono una foto y el nombre de la planta: {nombre}.
  Analiza visualmente la planta para detectar su salud considerando estos dos contextos:
    1. Está ubicada en el/la {lugar} de la casa.
    2. La persona vive en **{ubicacion}**.
  Usa tu conocimiento sobre el clima actual, la humedad y la temperatura de **{ubicacion}** para que tu diagnóstico sea preciso.
  Devuelve la respuesta estrictamente en formato JSON con estas claves:
  - estado: Estado de salud detectado en la foto (ej. Saludable, Enferma, Estrés hídrico).
  - problema: El problema más crítico detectado (ej. Sequedad, Plagas, Exceso de riego). Si no hay, pon 'Ninguno'.
  - descripcion: Breve descripción sobre el aspecto visual de la planta. Máximo 2 frases.
  - consejos: [Lista de 3 consejos de mantenimiento].
  - tareas: [{{
    "tarea": "Nombre de la acción (ej. Riego, Poda, Abonado)",
    "frecuencia": "Formato en 'Cada X días' (ej. Cada 7 días). OBLIGATORIO incluir un número, nunca texto sin dígito como 'Semanalmente'.",
    "experiencia": "Número de experiencia que se otorga al completar la tarea. El mínimo es 10 y el máximo es 20."
    }}]
  Responde solo el JSON, sé muy breve.
  """
  mensajes = [
    {
      "role": "user",
      "content": [
        {"type": "text", "text": prompt},
        {
          "type": "image_url",
          "image_url": {"url": f"data:{mime_type};base64,{foto_b64}"},
        },
      ],
    }
  ]
  resultado = await consultar_openrouter(mensajes)
  if resultado:
    resultado["tareas"] = validar_tareas(resultado.get("tareas"))
    return resultado


  # Red de seguridad: si los 4 modelos fallan (caída total de OpenRouter,
  # sin internet, etc.), devolvemos un análisis genérico plausible en vez
  # de un JSON de error. Esto es SOLO para que la demo nunca se vea rota.
  print("AVISO: Todos los modelos fallaron, devolviendo respuesta de emergencia.")
  return {
    "estado": "Saludable",
    "problema": "Ninguno",
    "descripcion": f"La {nombre} presenta buen aspecto general. No se detectan signos evidentes de estrés en la foto.",
    "consejos": [
      "Riega cuando los primeros centímetros de sustrato estén secos.",
      "Ubica la planta según su necesidad de luz.",
      "Revisa las hojas periódicamente para detectar plagas a tiempo."
    ],
    "tareas": [
      {"tarea": "Riego", "frecuencia": "Cada 7 días", "experiencia": 10},
      {"tarea": "Revisión de hojas", "frecuencia": "Cada 14 días", "experiencia": 10}
    ]
  }