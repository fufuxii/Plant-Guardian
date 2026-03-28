from pydantic import BaseModel, EmailStr
from typing import Optional

class Usuario(BaseModel):
  id_usuario: str

class UsuarioRegistro(BaseModel):
  nombre: str
  correo: EmailStr
  contraseña: str
  ubicacion: str

class UsuarioLogin(BaseModel):
  correo: EmailStr
  contraseña: str

class UsuarioActualizar(BaseModel):
  nombre: Optional[str] = None
  ubicacion: Optional[str] = None
  correo: Optional[str] = None

class IconoActualizar(BaseModel):
  url: str