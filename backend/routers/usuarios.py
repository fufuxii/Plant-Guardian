from fastapi import APIRouter, HTTPException
from logic.usuarios_logic import registrar_usuario, login_usuario, actualizar_usuario_datos, cambiar_usuario_icono, obtener_iconos_disponibles
from schemas import UsuarioRegistro, UsuarioLogin, UsuarioActualizar, IconoActualizar

router = APIRouter(prefix="/usuarios", tags=["Usuarios"])


@router.post("/auth/registro")
async def registro(usuario: UsuarioRegistro):
  resultado = await registrar_usuario(usuario)
  if "error" in resultado:
    raise HTTPException(status_code=400, detail=resultado["error"])
  return {
    "mensaje": "Usuario creado con éxito.",
    "id_usuario": resultado["id"],
  }


@router.post("/auth/login")
async def login(datos: UsuarioLogin):
  resultado = await login_usuario(datos)
  if isinstance(resultado, dict) and "error" in resultado:
    raise HTTPException(status_code=401, detail=resultado["error"])
  return {
    "mensaje": "Inicio de sesión correcto.",
    "usuario": resultado
  }


@router.patch("/actualizar/{id_usuario}")
async def editar_perfil(id_usuario: str, datos: UsuarioActualizar):
  datos_para_actualizar = datos.dict(exclude_unset=True)
  if not datos_para_actualizar:
    raise HTTPException(status_code=400, detail="Debe proporcionar al menos un campo para actualizar.")

  resultado = await actualizar_usuario_datos(id_usuario, datos_para_actualizar)
  if not resultado:
    raise HTTPException(status_code=404, detail="Usuario no encontrado.")
  
  return {
    "mensaje": "Perfil actualizado con éxito",
    "usuario": resultado
  }


@router.get("/{id_usuario}/iconos")
async def obtener_iconos(id_usuario: str):
  iconos = await obtener_iconos_disponibles(id_usuario)
  if iconos is None:
    raise HTTPException(status_code=404, detail="Usuario no encontrado.")
  return iconos


@router.patch("/{id_usuario}/icono")
async def actualizar_icono(id_usuario: str, datos: IconoActualizar):
  url_seleccionada = datos.url
  resultado = await cambiar_usuario_icono(id_usuario, url_seleccionada)
  if isinstance(resultado, dict) and "error" in resultado:
    raise HTTPException(status_code=403, detail=resultado["error"])
  return {"mensaje": "Icono actualizado.", "usuario": resultado}