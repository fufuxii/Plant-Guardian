import uuid
from fastapi import FastAPI, File, UploadFile, HTTPException
from database import supabase
from services.plantnet import identificar_planta
from services.gemini import analizar_planta

app = FastAPI(title="Plant Guardian API")
plantas_pendientes = {}

@app.get("/")
def inicio():
  return {"mensaje": "Plant Guardian API funcionando"}

@app.get("/plantas")
def get_obtener_plantas():
  response = supabase.table("Planta").select("*").execute()
  return {"plantas": response.data}

@app.post("/identificar")
async def post_identificar_planta(imagen: UploadFile = File(...)):
  contenido = await imagen.read()
  await imagen.seek(0) 
  resultado = await identificar_planta(imagen)
  
  temp_id = str(uuid.uuid4())
  plantas_pendientes[temp_id] = {
    "identificacion": resultado,
    "bytes_foto": contenido,
    "mime_type": imagen.content_type,
    "analizada": False
  }

  return {"temp_id": temp_id, "resultado": resultado}

@app.post("/analizar/{temp_id}")
async def post_analizar_planta(temp_id: str):
  if temp_id not in plantas_pendientes: 
    raise HTTPException(status_code=404, detail="ID no encontrado")
  
  registro = plantas_pendientes[temp_id]
  analisis_completo = await analizar_planta(
    nombre_planta=registro["identificacion"]["nombre_cientifico"],
    bytes_foto=registro["bytes_foto"],
    mime_type=registro["mime_type"]
  )

  registro["analisis_ia"] = analisis_completo
  registro["analizada"] = True

  return analisis_completo