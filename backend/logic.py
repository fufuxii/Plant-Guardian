import re
import uuid
from database import supabase
from datetime import datetime, timedelta
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


async def subir_planta_imagen(foto_bytes: bytes, mime_type: str):
  try:
    extension = mime_type.split("/")[-1]
    nombre_archivo = f"{uuid.uuid4()}.{extension}"
    supabase.storage.from_("plantas").upload(
      path=nombre_archivo,
      file=foto_bytes,
      file_options={"content-type": mime_type}
    )
    res_url = supabase.storage.from_("plantas").get_public_url(nombre_archivo)
    return res_url
  except Exception as e:
    print(f"Error subiendo imagen: {e}")
    return None


async def registrar_planta_usuario(id_usuario: str, id_planta: str, analisis: dict, 
                                  lugar: str, foto_bytes: bytes, mime_type: str,):
  url_imagen = await subir_planta_imagen(foto_bytes, mime_type)
  nueva_planta = {
    "id_usuario": id_usuario,
    "id_planta": id_planta,
    "imagen": url_imagen,
    "lugar": lugar,
    "estado": analisis["estado"],
    "problema": analisis["problema"],
    "descripcion": analisis["descripcion"],
    "tareas": analisis["tareas"],
    "consejos": analisis["consejos"],
    "fecha_obtencion": datetime.now().isoformat()
  }
  res_planta = supabase.table("Usuario_Planta").insert(nueva_planta).execute()
  id_registro = res_planta.data[0]["id"]
  await crear_planta_tareas(id_registro, analisis.get("tareas", []))
  return res_planta.data[0]


async def crear_planta_tareas(id_usuario_planta: str, tareas: list):
  if not tareas: return []
  fecha_base = datetime.now()
  filas_tareas = []
  for t in tareas:
    frecuencia_num = re.findall(r'\d+', t["frecuencia"])
    dias = int(frecuencia_num[0])
    filas_tareas.append({
      "id_usuario_planta": id_usuario_planta,
      "titulo": t["tarea"],
      "frecuencia_textual": t["frecuencia"],
      "frecuencia_numerica": dias,
      "fecha_proxima": (fecha_base + timedelta(days=dias)).isoformat(),
      "hecho": False
    })
  res = supabase.table("Tarea").insert(filas_tareas).execute()
  return res.data


async def reiniciar_tarea(id_tarea: str):
  res_tarea = supabase.table("Tarea").select("frecuencia_numerica").eq("id", id_tarea).execute()
  if not res_tarea.data: return None
  frecuencia = res_tarea.data[0]["frecuencia_numerica"]
  nueva_fecha = datetime.now() + timedelta(days=frecuencia)
  res_update = supabase.table("Tarea").update({
    "fecha_proxima": nueva_fecha.isoformat(),
    "hecho": False
  }).eq("id", id_tarea).execute()
  return res_update.data[0]