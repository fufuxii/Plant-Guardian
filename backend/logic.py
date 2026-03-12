from datetime import datetime
from database import supabase


async def registrar_planta(planta_info: dict):
  nueva_planta = {
    "nombre_cientifico": planta_info["nombre_cientifico"],
    "nombre_comun": planta_info["nombre_comun"]
  }

  res = supabase.table("Planta").insert(nueva_planta).execute()
  return res.data[0]["id"]


async def obtener_planta_id(planta_info: dict):
  nombre_cientifico = planta_info["nombre_cientifico"]
  query = supabase.table("Planta").select("id").eq("nombre_cientifico", nombre_cientifico).execute()

  if query.data: 
    return query.data[0]["id"]
  else: 
    return await registrar_planta(planta_info)


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