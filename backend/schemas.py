from pydantic import BaseModel, EmailStr

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