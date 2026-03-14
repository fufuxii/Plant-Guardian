from database import supabase
from passlib.context import CryptContext
from schemas import UsuarioRegistro, UsuarioLogin


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


async def registrar_usuario(datos: UsuarioRegistro):
  password_encriptada = pwd_context.hash(datos.contraseña)
  nuevo_usuario = {
    "nombre": datos.nombre,
    "correo": datos.correo,
    "contraseña": password_encriptada,
    "ubicacion": datos.ubicacion,
    "icono": "semilla_nivel_1",
    "nivel": 1,
    "experiencia": 0
  }

  try:
    existe = await supabase.table("Usuario").select("id").eq("correo", datos.correo).execute()
    if existe.data: return {"error": "Este correo ya está registrado."}
    res = await supabase.table("Usuario").insert(nuevo_usuario).execute()
    return res.data[0]
  except Exception as e:
    return {"error": str(e)}