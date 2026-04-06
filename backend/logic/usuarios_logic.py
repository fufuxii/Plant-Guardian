import re
import bcrypt
from database import supabase
from schemas import UsuarioRegistro, UsuarioLogin


def encriptar_password(password: str):
  salt = bcrypt.gensalt()
  password_bytes = password.encode('utf-8')
  hash_bytes = bcrypt.hashpw(password_bytes, salt)
  return hash_bytes.decode('utf-8')

def verificar_password(password_plana: str, password_encriptada: str):
  return bcrypt.checkpw(
    password_plana.encode('utf-8'), 
    password_encriptada.encode('utf-8')
  )

async def registrar_usuario(datos: UsuarioRegistro):
  try:
    icono_url = supabase.storage.from_("iconos").get_public_url("lvl1.png")
    password_encriptada = encriptar_password(datos.contraseña)
    nuevo_usuario = {
      "nombre": datos.nombre,
      "correo": datos.correo,
      "contraseña": password_encriptada,
      "ubicacion": datos.ubicacion,
      "icono": icono_url, 
      "nivel": 1,
      "experiencia_actual": 0,
      "experiencia_nivel": 100
    }
    usuario_bd = supabase.table("Usuario").insert(nuevo_usuario).execute()
    return usuario_bd.data[0]
  
  except Exception as e:
    error_msg = str(e)
    if "already exists" in error_msg or "23505" in error_msg or "duplicate" in error_msg:
      return {"error": "Este correo electrónico ya tiene una cuenta."}    
    return {"error": f"Error al crear la cuenta: {error_msg}"}
  

async def login_usuario(datos: UsuarioLogin):
  try:
    usuario_bd = supabase.table("Usuario").select("*").eq("correo", datos.correo).execute()
    if not usuario_bd.data: return {"error": "El correo no está registrado."}
    
    usuario = usuario_bd.data[0]
    if verificar_password(datos.contraseña, usuario["contraseña"]):
      usuario.pop("contraseña")
      return usuario
    else:
      return {"error": "Contraseña incorrecta."}
        
  except Exception as e:
    return {"error": "Error al intentar iniciar sesión."}
  

async def obtener_usuario_ubicacion(id_usuario: str):
  try:
    usuario_bd = supabase.table("Usuario").select("ubicacion").eq("id", id_usuario).execute()
    if usuario_bd.data: return usuario_bd.data[0]["ubicacion"]
    return "Desconocida"
  
  except Exception as e:
    print(f"Error al obtener ubicación: {e}")
    return "Desconocida"
  

async def actualizar_usuario_datos(id_usuario: str, datos_actualizados: dict):
  campos_permitidos = ["nombre", "ubicacion", "correo"]
  data_para_actualizar = {k: v for k, v in datos_actualizados.items() if k in campos_permitidos}

  if not data_para_actualizar:
    return {"error": "No hay datos válidos para actualizar."}

  res = supabase.table("Usuario")\
    .update(data_para_actualizar)\
    .eq("id", id_usuario).execute()

  return res.data[0] if res.data else None


async def obtener_iconos_disponibles(id_usuario: str):
  usuario_bd = supabase.table("Usuario").select("nivel").eq("id", id_usuario).execute()
  if not usuario_bd.data: return None

  nivel_actual = usuario_bd.data[0]["nivel"]
  bucket_name = "publico"
  folder_path = "iconos"
  iconos_elegibles = []
  
  for n in range(1, nivel_actual + 1):
    file_name = f"lvl{n}.png"
    url = supabase.storage.from_(bucket_name).get_public_url(f"{folder_path}/{file_name}")
    iconos_elegibles.append({
      "id": n,
      "url": url,
      "nombre": f"Nivel {n}"
    })
  
  return iconos_elegibles


async def cambiar_usuario_icono(id_usuario: str, url_seleccionada: str):
  match = re.search(r'lvl(\d+)\.png', url_seleccionada)
  if not match: return {"error": "Formato de icono no válido."}
  
  icono_lvl = int(match.group(1))
  user_res = supabase.table("Usuario").select("nivel").eq("id", id_usuario).execute()
  nivel_usuario = user_res.data[0]["nivel"]
  
  if icono_lvl > nivel_usuario:
    return {"error": "Aún no has alcanzado el nivel necesario para este icono."}
  
  usuario_bd = supabase.table("Usuario").update({"icono": url_seleccionada}).eq("id", id_usuario).execute()
  return usuario_bd.data[0]