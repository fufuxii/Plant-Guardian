from fastapi import FastAPI, File, UploadFile 
from database import supabase
from services.plantnet import identificar_planta

app = FastAPI(title="Plant Guardian API")

@app.get("/")
def inicio():
  return {"mensaje": "🌱 Plant Guardian API funcionando..."}

@app.get("/plantas")
def obtener_plantas():
  response = supabase.table("Planta").select("*").execute()
  return {"plantas": response.data}

@app.post("/identificar")
async def post_identificar(imagen: UploadFile = File(...)):
  resultado = await identificar_planta(imagen)
  return resultado