import re
import uuid
from database import supabase
from datetime import datetime, timedelta
from services.gemini import gemini_obtener_info_extra
from logic.logros_logic import gestionar_usuario_logros


async def registrar_planta(planta_info: dict):
  planta_info_extra = await gemini_obtener_info_extra(planta_info["nombre_cientifico"])
  nueva_planta = {
    "nombre_cientifico": planta_info["nombre_cientifico"],
    "nombre_comun": planta_info["nombre_comun"],
    "nombre_otros": planta_info_extra.get("nombre_otros"),
    "descripcion": planta_info_extra.get("descripcion") 
  }
  planta_bd = supabase.table("Planta").insert(nueva_planta).execute()
  return planta_bd.data[0]["id"]


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
    imagen_url = supabase.storage.from_("plantas").get_public_url(nombre_archivo)
    return imagen_url
  except Exception as e:
    print(f"Error subiendo imagen: {e}")
    return None


async def registrar_planta_usuario(id_usuario: str, id_planta: str, analisis: dict, 
                                  lugar: str, foto_bytes: bytes, mime_type: str,):
  imagen_url = await subir_planta_imagen(foto_bytes, mime_type)
  nueva_planta = {
    "id_usuario": id_usuario,
    "id_planta": id_planta,
    "imagen": imagen_url,
    "lugar": lugar,
    "estado": analisis["estado"],
    "problema": analisis["problema"],
    "descripcion": analisis["descripcion"],
    "tareas": analisis["tareas"],
    "consejos": analisis["consejos"],
    "fecha_obtencion": datetime.now().isoformat()
  }
  planta_bd = supabase.table("Usuario_Planta").insert(nueva_planta).execute()
  id_registro = planta_bd.data[0]["id"]
  await crear_planta_tareas(id_registro, analisis.get("tareas", []))
  logros_ganados = await verificar_planta_logros(id_usuario)
  return {"planta": planta_bd.data[0], "logros_nuevos": logros_ganados}


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
      "hecho": False,
      "experiencia": t["experiencia"]
    })
  res = supabase.table("Tarea").insert(filas_tareas).execute()
  return res.data


async def eliminar_planta_usuario(id_usuario_planta: str):
  try:
    planta_bd = supabase.table("Usuario_Planta").select("imagen").eq("id", id_usuario_planta).execute()
    if not planta_bd.data: return {"error": "La planta no existe."}
    url_imagen = planta_bd.data[0].get("imagen")
    supabase.table("Usuario_Planta").delete().eq("id", id_usuario_planta).execute()
    
    if url_imagen:
      nombre_archivo = url_imagen.split("/")[-1]
      supabase.storage.from_("plantas").remove([nombre_archivo])
    return {"mensaje": "Planta con datos eliminados correctamente."}

  except Exception as e:
    print(f"Error al eliminar planta: {e}")
    return {"error": "No se pudo eliminar la planta."}
  

async def verificar_planta_logros(id_usuario: str):
  res_count = supabase.table("Usuario_Planta")\
    .select("id", count="exact")\
    .eq("id_usuario", id_usuario).execute()
  total_plantas = res_count.count if res_count.count is not None else 0
  return await gestionar_usuario_logros(
    id_usuario=id_usuario, 
    tipo_logro="planta", 
    valor_actual=total_plantas
  )