from fastapi import APIRouter, HTTPException
from logic.tareas_logic import completar_tarea, obtener_tareas

router = APIRouter(prefix="/tareas", tags=["Tareas"])


@router.patch("/{id_tarea}/completar")
async def completar_planta_tarea(id_tarea: str):
  resultado = await completar_tarea(id_tarea)
  if not resultado: 
    raise HTTPException(status_code=404, detail="La tarea no existe.")
  return {"mensaje": "¡Tarea completada con éxito!", "data": resultado}


@router.get("/planta/{id_usuario_planta}")
async def obtener_planta_tareas(id_usuario_planta: str):
  tareas = await obtener_tareas(id_usuario_planta)
  if tareas is None:
    raise HTTPException(status_code=404, detail="No se encontraron tareas para esta planta.")
  return tareas