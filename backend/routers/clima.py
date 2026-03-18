from fastapi import APIRouter, HTTPException
from services.openweather import obtener_clima

router = APIRouter(prefix="/clima", tags=["Clima"])


@router.get("/test-clima/{ciudad}")
async def test_clima(ciudad: str):
  resultado = await obtener_clima(ciudad)
  if "error" in resultado:
    status = 404 if "not found" in resultado["error"].lower() else 500
    raise HTTPException(status_code=status, detail=resultado["error"]) 
  return resultado