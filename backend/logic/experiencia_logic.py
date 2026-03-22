from database import supabase


async def obtener_datos_progresion(id_usuario: str):
  usuario_bd = supabase.table("Usuario")\
    .select("experiencia_actual, experiencia_nivel, nivel")\
    .eq("id", id_usuario).execute()
  return usuario_bd.data[0] if usuario_bd.data else None


async def sumar_experiencia_usuario(id_usuario: str, xp_cantidad: int):
  user_data = await obtener_datos_progresion(id_usuario)
  if not user_data: return None
  
  xp_actual = user_data["experiencia_actual"] + xp_cantidad
  xp_necesaria = user_data["experiencia_nivel"]
  nivel_actual = user_data["nivel"]
  ha_subido_nivel = False

  while xp_actual >= xp_necesaria:
    ha_subido_nivel = True
    nivel_actual += 1
    xp_actual = xp_actual - xp_necesaria
    xp_necesaria = int(xp_necesaria * 1.2)

  supabase.table("Usuario").update({
    "experiencia_actual": xp_actual,
    "experiencia_nivel": xp_necesaria,
    "nivel": nivel_actual
  }).eq("id", id_usuario).execute()

  return {
    "nivel": nivel_actual,
    "experiencia_actual": xp_actual,
    "experiencia_nivel": xp_necesaria,
    "ha_subido_nivel": ha_subido_nivel,
    "xp_ganada": xp_cantidad
  }