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
  res_up = supabase.table("Usuario_Planta") \
    .select("id_usuario") \
    .eq("id", tarea["id_usuario_planta"]).execute()
  if not res_up.data: return None
  return {
    "id_usuario": res_up.data[0]["id_usuario"],
    "experiencia": tarea.get("experiencia", 10),
    "frecuencia": tarea["frecuencia_numerica"]
  }


async def actualizar_tarea_estado(id_tarea: str, frecuencia: int):
  nueva_fecha = datetime.now() + timedelta(days=frecuencia)
  res = supabase.table("Tarea").update({
    "fecha_proxima": nueva_fecha.isoformat(),
    "hecho": True 
  }).eq("id", id_tarea).execute()
  return res.data[0] if res.data else None


async def verificar_tarea_logros(id_usuario: str):
  res_plantas = supabase.table("Usuario_Planta").select("id").eq("id_usuario", id_usuario).execute()
  ids_plantas = [p["id"] for p in res_plantas.data]
  if not ids_plantas: return []
  res_count = supabase.table("Tarea").select("id", count="exact") \
    .in_("id_usuario_planta", ids_plantas) \
    .eq("hecho", True).execute()
  total_tareas = res_count.count if res_count.count else 0
  return await gestionar_usuario_logros(id_usuario, "tarea", total_tareas)


async def completar_tarea(id_tarea: str):
  datos = await obtener_tarea_datos(id_tarea)
  if not datos: return None
  tarea_act = await actualizar_tarea_estado(id_tarea, datos["frecuencia"])
  progreso = await sumar_experiencia_usuario(datos["id_usuario"], datos["experiencia"])
  logros_tareas = await verificar_tarea_logros(datos["id_usuario"])
  logros_totales = progreso.get("logros_nuevos", []) + logros_tareas
  return {
    "tarea": tarea_act,
    "progreso": {
      "nivel": progreso["nivel"],
      "exp_actual": progreso["experiencia_actual"],
      "exp_nivel": progreso["experiencia_nivel"],
      "subio_nivel": progreso["ha_subido_nivel"],
      "logros_nuevos": logros_totales
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