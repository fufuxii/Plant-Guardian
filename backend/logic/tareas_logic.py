from database import supabase
from datetime import datetime, timedelta
from logic.experiencia_logic import sumar_experiencia_usuario


async def completar_tarea(id_tarea: str):
  res_tarea = supabase.table("Tarea") \
    .select("frecuencia_numerica, experiencia, id_usuario_planta") \
    .eq("id", id_tarea).execute()
  if not res_tarea.data: return None
  
  tarea = res_tarea.data[0]
  puntos_a_sumar = tarea.get("experiencia", 10)
  res_usuario_planta = supabase.table("Usuario_Planta") \
    .select("id_usuario") \
    .eq("id", tarea["id_usuario_planta"]).execute()
  if not res_usuario_planta.data: return None
  
  id_usuario = res_usuario_planta.data[0]["id_usuario"]
  nueva_fecha = datetime.now() + timedelta(days=tarea["frecuencia_numerica"])
  res_update = supabase.table("Tarea").update({
    "fecha_proxima": nueva_fecha.isoformat(),
    "hecho": True 
  }).eq("id", id_tarea).execute()

  progreso = await sumar_experiencia_usuario(id_usuario, puntos_a_sumar)
  return {"tarea": res_update.data[0], "progreso": progreso}


async def obtener_tareas(id_usuario_planta: str):
  fecha_actual = datetime.now().isoformat()
  supabase.table("Tarea") \
    .update({"hecho": False}) \
    .lte("fecha_proxima", fecha_actual) \
    .eq("hecho", True) \
    .execute()
  res = supabase.table("Tarea").eq("id_usuario_planta", id_usuario_planta).execute()
  return res.data