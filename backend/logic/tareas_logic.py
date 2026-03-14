from database import supabase
from datetime import datetime, timedelta

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