import uuid
from fastapi import APIRouter, HTTPException, UploadFile, File
from logic.plantas_logic import obtener_planta_id, registrar_planta_usuario, eliminar_planta_usuario
from logic.usuarios_logic import obtener_usuario_ubicacion
from services.plantnet import plantnet_identificar_planta
from services.gemini import gemini_analizar_planta
from schemas import Usuario

router = APIRouter(prefix="/plantas", tags=["Plantas"])
plantas_pendientes = {}


@router.post("/identificar")
async def identificar_planta(imagen: UploadFile = File(...)):
  contenido = await imagen.read()
  await imagen.seek(0) 
  resultado = await plantnet_identificar_planta(imagen)
  temp_id = str(uuid.uuid4())
  plantas_pendientes[temp_id] = {
    "informacion": resultado,
    "foto_bytes": contenido,
    "mime_type": imagen.content_type,
    "analizada": False
  }
  return {"temp_id": temp_id, "resultado": resultado}


@router.post("/analizar/{temp_id}")
async def analizar_planta(temp_id: str, lugar: str, id_usuario: str):
  if temp_id not in plantas_pendientes: 
    raise HTTPException(status_code=404, detail="ID no encontrado.")
  
  ubicacion = await obtener_usuario_ubicacion(id_usuario)
  planta = plantas_pendientes[temp_id]
  analisis = await gemini_analizar_planta(
    nombre=planta["informacion"]["nombre_cientifico"], 
    lugar=lugar, 
    ubicacion=ubicacion,
    foto_bytes=planta["foto_bytes"], 
    mime_type=planta["mime_type"]
  )

  planta["lugar"] = lugar
  planta["analisis"] = analisis
  planta["analizada"] = True
  return analisis


@router.post("/guardar/{temp_id}")
async def guardar_planta(temp_id: str, usuario: Usuario):
  planta = plantas_pendientes.get(temp_id)
  if not planta: 
    raise HTTPException(status_code=404, detail="El análisis de la planta ha expirado.")

  try:
    id_planta = await obtener_planta_id(planta["informacion"])
    resultado = await registrar_planta_usuario(
      id_usuario=usuario.id_usuario,
      id_planta=id_planta,
      analisis=planta["analisis"],
      lugar=planta["lugar"],
      foto_bytes=planta["foto_bytes"], 
      mime_type=planta["mime_type"]
    )
    del plantas_pendientes[temp_id]
    return {"mensaje": "¡Planta añadida con éxito!", "data": resultado}

  except Exception as e:
    print(f"Error en la confirmación: {e}")
    raise HTTPException(status_code=500, detail="Error interno al procesar el guardado.")
  

@router.delete("/{id_usuario_planta}")
async def eliminar_planta(id_usuario_planta: str):
  resultado = await eliminar_planta_usuario(id_usuario_planta)
  if "error" in resultado:
    raise HTTPException(status_code=400, detail=resultado["error"])
  return resultado