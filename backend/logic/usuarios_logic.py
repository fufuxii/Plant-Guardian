import bcrypt
from database import supabase
from passlib.context import CryptContext
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
    password_encriptada = encriptar_password(datos.contraseña)
    nuevo_usuario = {
      "nombre": datos.nombre,
      "correo": datos.correo,
      "contraseña": password_encriptada,
      "ubicacion": datos.ubicacion,
      "icono": "default", 
      "nivel": 1,
      "experiencia": 0
    }
    res = supabase.table("Usuario").insert(nuevo_usuario).execute()
    return res.data[0]
  
  except Exception as e:
    error_msg = str(e)
    if "already exists" in error_msg or "23505" in error_msg or "duplicate" in error_msg:
      return {"error": "Este correo electrónico ya tiene una cuenta."}    
    return {"error": f"Error al crear la cuenta: {error_msg}"}
  

async def login_usuario(datos: UsuarioLogin):
  try:
    res = supabase.table("Usuario").select("*").eq("correo", datos.correo).execute()
    if not res.data: return {"error": "El correo no está registrado."}
    
    usuario_db = res.data[0]
    if verificar_password(datos.contraseña, usuario_db["contraseña"]):
      usuario_db.pop("contraseña")
      return usuario_db
    else:
      return {"error": "Contraseña incorrecta."}
        
  except Exception as e:
    return {"error": "Error al intentar iniciar sesión."}