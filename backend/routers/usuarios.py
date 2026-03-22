from fastapi import APIRouter, HTTPException
from logic.usuarios_logic import registrar_usuario, login_usuario
from schemas import UsuarioRegistro, UsuarioLogin

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