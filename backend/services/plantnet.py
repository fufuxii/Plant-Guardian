import httpx
import os

API_KEY = os.getenv("PLANTNET_API_KEY")
URL = f"https://my-api.plantnet.org/v2/identify/all?api-key={API_KEY}"

async def identificar_planta(archivo_imagen):
  files = {'images': (archivo_imagen.filename, archivo_imagen.file, archivo_imagen.content_type)}
  
  async with httpx.AsyncClient() as client:
    response = await client.post(URL, files=files)
    
    if response.status_code == 200:
      data = response.json()
      mejor_resultado = data['results'][0]
      return {
        "nombre_cientifico": mejor_resultado['species']['scientificNameWithoutAuthor'],
        "nombre_comun": mejor_resultado['species']['commonNames'][0] if mejor_resultado['species']['commonNames'] else "Desconocido"
      }
    else:
      return {"error": "No se pudo identificar la planta."}