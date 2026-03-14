from datetime import datetime
from database import supabase
from services.gemini import obtener_info_extra


async def registrar_planta(planta_info: dict):
  planta_info_extra = await obtener_info_extra(planta_info["nombre_cientifico"])
  nueva_planta = {
    "nombre_cientifico": planta_info["nombre_cientifico"],
    "nombre_comun": planta_info["nombre_comun"],
    "nombre_otros": planta_info_extra.get("nombre_otros"),
    "descripcion": planta_info_extra.get("descripcion") 
  }
  res = supabase.table("Planta").insert(nueva_planta).execute()
  return res.data[0]["id"]


async def obtener_planta_id(planta_info: dict):
  nombre_cientifico = planta_info["nombre_cientifico"]
  query = supabase.table("Planta").select("id").eq("nombre_cientifico", nombre_cientifico).execute()
  return query.data[0]["id"] if query.data else await registrar_planta(planta_info)


async def registrar_planta_usuario(id_usuario: str, id_planta: str, analisis: dict, lugar: str, imagen: str):
  nueva_planta = {
    "id_usuario": id_usuario,
    "id_planta": id_planta,
    "imagen": imagen,
    "lugar": lugar,
    "estado": analisis["estado"],
    "problema": analisis["problema"],
    "descripcion": analisis["descripcion"],
    "tareas": analisis["tareas"],
    "consejos": analisis["consejos"],
    "fecha_obtencion": datetime.now().isoformat()
  }
  res = supabase.table("Usuario_Planta").insert(nueva_planta).execute()
  return res.data[0]