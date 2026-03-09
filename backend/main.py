from fastapi import FastAPI
from database import supabase

app = FastAPI(title="Plant Guardian API")

@app.get("/")
def inicio():
  return {"mensaje": "🌱 Plant Guardian API funcionando..."}

@app.get("/plantas")
def obtener_plantas():
  response = supabase.table("Planta").select("*").execute()
  return {"plantas": response.data}