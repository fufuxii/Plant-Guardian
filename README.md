# 🌿 Plant Guardian

**Plant Guardian** es una aplicación móvil para el cuidado inteligente de plantas que combina Inteligencia Artificial y gamificación para transformar la jardinería en una experiencia interactiva y educativa.

---

## Características principales

- **Diagnóstico con IA:** Integración con Google Gemini AI para analizar la salud de las plantas mediante fotos.
- **Identificación botánica:** Reconocimiento automático de especies a través de la API de Pl@ntNet.
- **Gamificación:** Sistema de niveles, puntos de experiencia (XP), logros y avatares desbloqueables.
- **Gestión de tareas:** Calendario de riego y cuidados adaptado al clima local mediante OpenWeather.

---

## Tecnología

### Backend

| Elemento       | Detalle                        |
|----------------|-------------------------------|
| Lenguaje       | Python 3.10+                  |
| Framework      | FastAPI                       |
| Base de datos  | Supabase (PostgreSQL)         |
| Autenticación  | JWT & Bcrypt                  |
| Arquitectura   | Arquitectura por capas        |

### Frontend

| Elemento     | Detalle              |
|--------------|----------------------|
| Lenguaje     | Kotlin               |
| Entorno      | Android Studio       |
| Arquitectura | MVVM                 |

---

## Estructura del proyecto

```text
PlantGuardian/
├── backend/           # API REST construida con FastAPI
│   ├── logic/         # Lógica de negocio (XP, Logros, Tareas)
│   ├── routers/       # Endpoints de la API
│   ├── services/      # Integración con APIs externas (Gemini, Weather)
│   └── database.py    # Conexión con Supabase
├── frontend/          # Proyecto Android Studio (Kotlin)
├── docs/              # Documentación e informes de progreso
├── imgs/              # Imágenes e iconos del proyecto
└── .gitignore
```

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/PlantGuardian.git
cd PlantGuardian/backend
```

### 2. Crear el entorno virtual

```bash
# Crear
python -m venv venv

# Activar en Windows
venv\Scripts\activate

# Activar en Mac/Linux
source venv/bin/activate
```

### 3. Instalar dependencias

```bash
pip install -r requirements.txt
```

### 4. Variables de entorno

Crea un archivo `.env` dentro de `backend/`:

```env
SUPABASE_URL=tu_url
SUPABASE_KEY=tu_key
GEMINI_API_KEY=tu_api_key
OPENWEATHER_API_KEY=tu_api_key
```

---

## Ejecución

```bash
uvicorn main:app --reload
```

El servidor estará disponible en `http://127.0.0.1:8000`.