import uuid
from logic import *
from pydantic import BaseModel
from services.plantnet import identificar_planta
from services.gemini import analizar_planta
from fastapi import FastAPI, File, UploadFile, HTTPException

app = FastAPI(title="Plant Guardian API")
plantas_pendientes = {}


class Datos(BaseModel):
  id_usuario: str


@app.get("/")
def inicio():
  return {"mensaje": "Plant Guardian API funcionando."}


@app.post("/identificar")
async def post_identificar_planta(imagen: UploadFile = File(...)):
  contenido = await imagen.read()
  await imagen.seek(0) 
  resultado = await identificar_planta(imagen)

  temp_id = str(uuid.uuid4())
  plantas_pendientes[temp_id] = {
    "informacion": resultado,
    "foto_bytes": contenido,
    "mime_type": imagen.content_type,
    "analizada": False
  }

  return {"temp_id": temp_id, "resultado": resultado}


@app.post("/analizar/{temp_id}")
async def post_analizar_planta(temp_id: str, lugar: str):
  if temp_id not in plantas_pendientes: 
    raise HTTPException(status_code=404, detail="ID no encontrado.")
  
  planta = plantas_pendientes[temp_id]
  analisis = await analizar_planta(
    nombre=planta["informacion"]["nombre_cientifico"], 
    lugar=lugar, 
    foto_bytes=planta["foto_bytes"], 
    mime_type=planta["mime_type"]
  )

  planta["lugar"] = lugar
  planta["analisis"] = analisis
  planta["analizada"] = True
  return analisis


@app.post("/guardar/{temp_id}")
async def post_guardar_planta(temp_id: str, datos: Datos):
  planta = plantas_pendientes.get(temp_id)
  
  if not planta:
    raise HTTPException(status_code=404, detail="El análisis de la planta ha expirado.")

  try:
    id_planta = await obtener_planta_id(planta["informacion"])
    resultado = await registrar_planta_usuario(
      id_usuario=datos.id_usuario,
      id_planta=id_planta,
      analisis=planta["analisis"],
      lugar=planta["lugar"],
      imagen=None
    )

    del plantas_pendientes[temp_id]
    return {"mensaje": "¡Planta añadida con éxito!", "data": resultado}

  except Exception as e:
    print(f"Error en la confirmación: {e}")
    raise HTTPException(status_code=500, detail="Error interno al procesar el guardado.")