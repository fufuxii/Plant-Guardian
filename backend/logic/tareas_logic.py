from database import supabase
from datetime import datetime, timedelta
from logic.experiencia_logic import sumar_experiencia_usuario
from logic.logros_logic import gestionar_usuario_logros


async def obtener_tarea_datos(id_tarea: str):
  res_tarea = supabase.table("Tarea") \
    .select("frecuencia_numerica, experiencia, id_usuario_planta") \
    .eq("id", id_tarea).execute()
  if not res_tarea.data: return None
  tarea = res_tarea.data[0]
  res_usuario_planta = supabase.table("Usuario_Planta") \
    .select("id_usuario") \
    .eq("id", tarea["id_usuario_planta"]).execute()
  if not res_usuario_planta.data: return None
  return {
    "id_usuario": res_usuario_planta.data[0]["id_usuario"],
    "experiencia": tarea.get("experiencia", 10),
    "frecuencia": tarea["frecuencia_numerica"]
  }


async def actualizar_tarea_estado(id_tarea: str, frecuencia: int):
  nueva_fecha = datetime.now() + timedelta(days=frecuencia)
  res_tarea = supabase.table("Tarea").update({
    "fecha_proxima": nueva_fecha.isoformat(),
    "hecho": True 
  }).eq("id", id_tarea).execute()
  return res_tarea.data[0] if res_tarea.data else None


async def incrementar_tareas_completadas(id_usuario: str):
  res_usuario = supabase.table("Usuario").select("tareas_completadas").eq("id", id_usuario).execute()
  tareas_completadas_actuales = res_usuario.data[0]["tareas_completadas"] if res_usuario.data else 0
  tareas_completadas_total = tareas_completadas_actuales + 1
  supabase.table("Usuario").update({"tareas_completadas": tareas_completadas_total}).eq("id", id_usuario).execute()
  return tareas_completadas_total


async def verificar_tarea_logros(id_usuario: str, total_tareas: int):
  return await gestionar_usuario_logros(id_usuario, "tarea", total_tareas)


async def completar_tarea(id_tarea: str):
  datos = await obtener_tarea_datos(id_tarea)
  if not datos: return None
  tarea = await actualizar_tarea_estado(id_tarea, datos["frecuencia"])
  tareas_completadas_total = await incrementar_tareas_completadas(datos["id_usuario"])
  progreso = await sumar_experiencia_usuario(datos["id_usuario"], datos["experiencia"])
  logros_tareas = await verificar_tarea_logros(datos["id_usuario"], tareas_completadas_total)
  logros_totales = progreso.get("logros_nuevos", []) + logros_tareas
  return {
    "tarea": tarea,
    "progreso": {
      "nivel": progreso["nivel"],
      "exp_actual": progreso["experiencia_actual"],
      "exp_nivel": progreso["experiencia_nivel"],
      "subio_nivel": progreso["ha_subido_nivel"],
      "logros_nuevos": logros_totales,
      "tareas_completadas": tareas_completadas_total
    }
  }


async def obtener_tareas(id_usuario_planta: str):
  fecha_actual = datetime.now().isoformat()
  supabase.table("Tarea") \
    .update({"hecho": False}) \
    .lte("fecha_proxima", fecha_actual) \
    .eq("hecho", True) \
    .execute()
  res = supabase.table("Tarea").eq("id_usuario_planta", id_usuario_planta).execute()
  return res.data