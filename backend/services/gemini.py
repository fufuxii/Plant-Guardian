from google import genai
from google.genai import types
import os
import json
from dotenv import load_dotenv

load_dotenv()
client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

async def analizar_planta(nombre_planta: str, bytes_foto: bytes, mime_type: str):
  ID_MODELO = "gemini-flash-latest"

  prompt = f"""
    Actúa como un experto botánico. Te proporciono una foto y el nombre de la planta: {nombre_planta}.
    Analiza visualmente la planta en la foto para detectar su salud actual.
    Devuelve la respuesta estrictamente en formato JSON con estas claves:
    - estado: Estado de salud detectado en la foto (ej. Saludable, Enferma, Estrés hídrico).
    - problema: El problema más crítico detectado (ej. Sequedad, Plagas, Exceso de riego). Si no hay, pon 'Ninguno'.
    - descripcion: Breve descripción sobre el aspecto visual de la planta. Máximo 2 frases.
    - recomendaciones: [Lista de 3 consejos de mantenimiento].
    - tareas_mantenimiento: [{{"tarea": "Nombre de la acción (ej. Riego, Poda, Abonado)", "frecuencia": "Formato en 'Cada X días' (ej. Cada 7 días)"}}]
    Responde solo el JSON, sé muy breve.
    """
  
  foto = types.Part.from_bytes(data=bytes_foto, mime_type=mime_type)
  
  # Se prueban varios modelos porque entre pruebas quizás se sobrepase los créditos gratuitos.
  modelos = ["gemini-flash-latest", "gemini-2.0-flash", "gemini-3-flash-preview"]
  for modelo in modelos:
    try:
      print(f"Intentando con el modelo: {modelo}")
      response = client.models.generate_content(model=ID_MODELO, contents=[prompt, foto])
      texto = response.text
      inicio = texto.find('{')
      fin = texto.rfind('}') + 1
      return json.loads(texto[inicio:fin])
    
    except Exception as e:
      print(f"Falló el modelo {modelo}: {e}")
      continue
  
  return {"error": "Ningún modelo disponible tiene cuota ahora mismo"}