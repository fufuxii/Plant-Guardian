import httpx
import os

API_KEY = os.getenv("PLANTNET_API_KEY")
URL = "https://my-api.plantnet.org/v2/identify/all"


async def plantnet_identificar_planta(imagen):
  params = {"api-key": API_KEY, "lang": "es"}
  files = {'images': (imagen.filename, imagen.file, imagen.content_type)}
  
  async with httpx.AsyncClient() as client:
    try:
      resultado = await client.post(URL, params=params, files=files)
      
      if resultado.status_code == 200:
        data = resultado.json()
        if not data.get('results'): 
          return {"error": "No se encontraron coincidencias para esta imagen."}
        mejor_resultado = data['results'][0]
        especie = mejor_resultado.get('species', {})
        return {
          "nombre_cientifico": especie.get('scientificNameWithoutAuthor', "Desconocido"),
          "nombre_comun": especie.get('commonNames', ["Desconocido"])[0] 
        }
      
      else:
        print(f"Error Pl@ntNet: {resultado.status_code} - {resultado.text}")
        return {"error": f"Error de la API: {resultado.status_code}"}
    
    except Exception as e:
      print(f"Error de conexión en identificar_planta: {e}")
      return {"error": "Error de conexión con el servicio de identificación."}