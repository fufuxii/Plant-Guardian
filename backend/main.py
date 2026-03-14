from fastapi import FastAPI
from routers import usuarios, plantas, tareas

app = FastAPI(
  title="Plant Guardian API",
  description="API para el cuidado de plantas con IA y Gamificación",
)

app.include_router(usuarios.router)
app.include_router(plantas.router)
app.include_router(tareas.router)

@app.get("/")
def inicio():
  return {
    "mensaje": "Plant Guardian API funcionando.",
    "estado": "Online",
    "documentacion": "/docs"
  }