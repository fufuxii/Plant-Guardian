from database import supabase
from datetime import datetime


async def obtener_logros_restantes(id_usuario: str, tipo_logro: str, valor_actual: int):
  logros_obtenidos = supabase.table("Usuario_Logro")\
    .select("id_logro")\
    .eq("id_usuario", id_usuario).execute()
  ids_logros_obtenidos = [l["id_logro"] for l in logros_obtenidos.data]
  query = supabase.table("Logro").select("*")\
    .eq("tipo", tipo_logro)\
    .lte("requisito", valor_actual)
  if ids_logros_obtenidos: query = query.not_.in_("id", ids_logros_obtenidos)
  res = query.execute()
  return res.data


async def registrar_logros_nuevos(id_usuario: str, lista_logros: list):
  logros_guardados = []
  for logro in lista_logros:
    res = supabase.table("Usuario_Logro").insert({
      "id_usuario": id_usuario,
      "id_logro": logro["id"],
      "fecha_obtencion": datetime.now().isoformat()
    }).execute() 
    if res.data: 
      logros_guardados.append(logro)
      print(f"DEBUG: Logro otorgado correctamente: {logro['titulo']}.")
  return logros_guardados


async def gestionar_usuario_logros(id_usuario: str, tipo_logro: str, valor_actual: int):
  print(f"DEBUG: Verificación de logros. Tipo: {tipo_logro}, Valor: {valor_actual}.")
  pendientes = await obtener_logros_restantes(id_usuario, tipo_logro, valor_actual)
  if not pendientes: return []
  nuevos = await registrar_logros_nuevos(id_usuario, pendientes)
  return nuevos