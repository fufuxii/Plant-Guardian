import httpx
import os

API_KEY = os.getenv("OPENWEATHER_API_KEY")
BASE_URL = "https://api.openweathermap.org/data/2.5/weather"


async def obtener_clima(ubicacion: str):
  params = {
    "q": ubicacion,
    "appid": API_KEY,
    "units": "metric",
    "lang": "es"
  }

  async with httpx.AsyncClient() as client:
    try:
      response = await client.get(BASE_URL, params=params)
      
      if response.status_code == 200:
        data = response.json()
        return {
          "clima_id": data["weather"][0]["id"],
          "temp": data["main"]["temp"],
          "humedad": data["main"]["humidity"],
          "viento": data["wind"]["speed"],
          "lluvia": data.get("rain", {}).get("1h", 0.0),
          "descripcion": data["weather"][0]["description"],
          "icono": f"https://openweathermap.org/img/wn/{data['weather'][0]['icon']}@2x.png",
        }
      
      elif response.status_code == 404:
        return {"error": "Ciudad no encontrada."}
      
      else:
        return {"error": "No se pudo obtener el clima."}
    
    except Exception as e:
      print(f"Error en OpenWeather: {e}")
      return {"error": "Error de conexión con el servicio meteorológico."}