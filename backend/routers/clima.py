from fastapi import APIRouter, HTTPException
from services.openweather import openweather_obtener_clima

router = APIRouter(tags=["Clima"])


@router.get("/clima/{ciudad}")
async def obtener_clima(ciudad: str):
  resultado = await openweather_obtener_clima(ciudad)
  if "error" in resultado:
    status = 404 if "not found" in resultado["error"].lower() else 500
    raise HTTPException(status_code=status, detail=resultado["error"]) 
  return resultado