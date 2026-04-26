from uuid import UUID
from database import supabase
from fastapi import APIRouter, HTTPException
from services.openweather import openweather_obtener_clima

router = APIRouter(tags=["Clima"])


@router.get("/clima/usuario/{user_id}")
async def obtener_clima(user_id: UUID):
  usuario_id = supabase.table("Usuario").select("ubicacion").eq("id", user_id).execute()
  if not usuario_id.data:
    raise HTTPException(status_code=404, detail="Usuario no encontrado")
  
  usuario_ciudad = usuario_id.data[0].get("ubicacion")
  if not usuario_ciudad:
    raise HTTPException(status_code=400, detail="El usuario no tiene una ciudad configurada")
  
  clima = await openweather_obtener_clima(usuario_ciudad)
  if "error" in clima:
    raise HTTPException(status_code=500, detail=clima["error"])
  
  return clima