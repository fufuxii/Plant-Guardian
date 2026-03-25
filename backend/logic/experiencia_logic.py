from database import supabase
from logic.logros_logic import gestionar_usuario_logros


async def obtener_datos_progresion(id_usuario: str):
  usuario_bd = supabase.table("Usuario")\
    .select("experiencia_actual, experiencia_nivel, nivel")\
    .eq("id", id_usuario).execute()
  return usuario_bd.data[0] if usuario_bd.data else None


def calcular_progresion(xp_actual: int, xp_necesaria: int, nivel_actual: int, xp_ganada: int):
  total_xp = xp_actual + xp_ganada
  ha_subido_nivel = False
  while total_xp >= xp_necesaria:
    ha_subido_nivel = True
    nivel_actual += 1
    total_xp -= xp_necesaria
    xp_necesaria = int(xp_necesaria * 1.2)
  return nivel_actual, total_xp, xp_necesaria, ha_subido_nivel


async def actualizar_progresion(id_usuario: str, nivel: int, xp: int, xp_nivel: int):
  return supabase.table("Usuario").update({
    "experiencia_actual": xp,
    "experiencia_nivel": xp_nivel,
    "nivel": nivel
  }).eq("id", id_usuario).execute()


async def verificar_nivel_logros(id_usuario: str, nivel_actual: int, ha_subido_nivel: bool):
  if not ha_subido_nivel: return [] 
  return await gestionar_usuario_logros(
    id_usuario=id_usuario, 
    tipo_logro="nivel", 
    valor_actual=nivel_actual
  )


async def sumar_experiencia_usuario(id_usuario: str, xp_cantidad: int):
  user_data = await obtener_datos_progresion(id_usuario)
  if not user_data: return None
  nuevo_nivel, nueva_xp, nueva_xp_necesaria, subio = calcular_progresion(
    user_data["experiencia_actual"],
    user_data["experiencia_nivel"],
    user_data["nivel"],
    xp_cantidad
  )
  await actualizar_progresion(id_usuario, nuevo_nivel, nueva_xp, nueva_xp_necesaria)
  logros = await verificar_nivel_logros(id_usuario, nuevo_nivel, subio)
  return {
    "nivel": nuevo_nivel,
    "experiencia_actual": nueva_xp,
    "experiencia_nivel": nueva_xp_necesaria,
    "ha_subido_nivel": subio,
    "xp_ganada": xp_cantidad,
    "logros_nuevos": logros
  }