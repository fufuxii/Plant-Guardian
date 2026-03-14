from fastapi import APIRouter, HTTPException
from logic.tareas_logic import reiniciar_tarea

router = APIRouter(prefix="/tareas", tags=["Tareas"])

@router.patch("/{id_tarea}/completar")
async def patch_completar_tarea(id_tarea: str):
  resultado = await reiniciar_tarea(id_tarea)
  if not resultado: 
    raise HTTPException(status_code=404, detail="La tarea no existe.")
  return {"mensaje": "¡Tarea completada!", "data": resultado}