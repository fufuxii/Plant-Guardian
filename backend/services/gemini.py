import os
import json
from google import genai
from google.genai import types
from dotenv import load_dotenv

load_dotenv()
CLIENT = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))
MODELOS_IA = ["gemini-flash-latest", "gemini-2.0-flash", "gemini-3-flash-preview"]


async def consultar_gemini(contenido: list):
  for modelo in MODELOS_IA:
    try:
      print(f"Intentando con el modelo: {modelo}")
      response = CLIENT.models.generate_content(model=modelo, contents=contenido)
      texto = response.text
      inicio = texto.find('{')
      fin = texto.rfind('}') + 1
      if inicio == -1 or fin == 0:
        raise ValueError("Respuesta sin formato JSON.")
      return json.loads(texto[inicio:fin])
    except Exception as e:
      print(f"Falló el modelo {modelo}: {e}")
      continue 
  return None


async def gemini_obtener_info_extra(nombre: str):
  prompt = f"""
  Eres una enciclopedia botánica. Proporciona información sobre la especie: {nombre}.
  Devuelve estrictamente un JSON con:
  - nombre_otros: Otros nombres comunes o populares en diferentes países de habla hispana (separados por comas).
  - descripcion: Una descripción botánica general, muy breve (máximo 20 palabras).
  Responde solo el JSON.
  """
  resultado = await consultar_gemini([prompt])
  return resultado or {
    "nombre_otros": "No hay otros nombres disponibles.", 
    "descripcion": "Información botánica en proceso de actualización."
  }


async def gemini_analizar_planta(nombre: str, lugar: str, ubicacion: str, foto_bytes: bytes, mime_type: str):
  foto = types.Part.from_bytes(data=foto_bytes, mime_type=mime_type)
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
  - tareas: [{{"tarea": "Nombre de la acción (ej. Riego, Poda, Abonado)", "frecuencia": "Formato en 'Cada X días' (ej. Cada 7 días)"}}]
  Responde solo el JSON, sé muy breve.
  """
  resultado = await consultar_gemini([prompt, foto])
  return resultado if resultado else {"error": "Lo sentimos, el servicio de diagnóstico por IA no está disponible ahora mismo."}