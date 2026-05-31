<div align="center">

<img src="imgs/logo_plant_guardian.png" width="120"/>

# Plant Guardian

**Aplicación móvil para el cuidado inteligente de plantas de exterior e interior**

![Python](https://img.shields.io/badge/Python-3.10+-3776AB?logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?logo=fastapi&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?logo=kotlin&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3ECF8E?logo=supabase&logoColor=white)
![Estado](https://img.shields.io/badge/Estado-MVP%20Funcional-5adf72)

Fiorella Lucia Queirolo Chavez · Grado en Ingeniería Informática · Universidad Autónoma de Barcelona (UAB)

</div>

## 🌿 Descripción

Plant Guardian es un **proyecto de final de grado (TFG)** que combina la Inteligencia Artificial con mecánicas de gamificación para transformar el mantenimiento botánico en una experiencia interactiva y personalizada.

## ✨ Características

- **Diagnóstico con IA** — Análisis avanzado de la salud de las plantas mediante fotos usando Google Gemini AI.
- **Identificación botánica** — Reconocimiento automático de especies a través de la API de Pl@ntNet.
- **Gamificación** — Sistema de niveles, puntos XP, logros desbloqueables y avatares personalizados.
- **Gestión de tareas** — Tareas personalizadas para cada planta según su estado, el lugar y el clima local.
- **Repositorio centralizado** — Seguimiento individualizado de cada planta con persistencia de datos en la nube.

## 🛠️ Tecnología

**Backend** 
- Lenguaje — Python 3.10+
- Framework — FastAPI 
- Base de datos — Supabase (PostgreSQL)
- Arquitectura — Por capas (Routers, Services, Logic)

**Frontend** 
- Lenguaje — Kotlin
- Entorno — Android Studio
- Arquitectura — MVVM
- Comunicación API — Retrofit
- Diseño — Material Design 3

**APIs externas**
- [Gemini](https://ai.google.dev/) — Inteligencia artificial
- [Pl@ntNet](https://my.plantnet.org) — Base de datos de plantas
- [OpenWeather](https://openweathermap.org/) — Datos meteorológicos

## 📈 Desarrollo

El proyecto se ha desarrollado bajo una metodología **Ágil (SCRUM)**, dividido en 4 sprints. Se ha utilizado IA Generativa con Gemini y Claude como soporte para la optimización de código y mejora en el diseño de las interfaces.

## 📁 Estructura del proyecto

```
PlantGuardian/
├── backend/                # API REST (FastAPI)
│   ├── logic/              # Algoritmos de XP, Logros y Tareas
│   ├── routers/            # Definición de Endpoints de la API
│   ├── services/           # Integración de APIs externas (Gemini, Weather)
│   ├── schemas/            # Modelos de validación de datos (Pydantic)
│   └── database.py         # Configuración y conexión con Supabase
├── frontend/               # Aplicación Android (Kotlin)
│   ├── app/src/main/java/  # Código fuente organizado por paquetes (ui, data, tools)
│   └── app/src/main/res/   # Recursos visuales, layouts XML y estilos
├── docs/                   # Informes de progreso y documentación del TFG
└── imgs/                   # Activos gráficos (logos, iconos de logros y avatares)
```

## 🚀 Instalación en local

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/PlantGuardian.git
cd PlantGuardian/backend
```

### 2. Crear el entorno virtual

```bash
python -m venv venv
source venv/bin/activate        # Mac/Linux
venv\Scripts\activate           # Windows
```

### 3. Instalar dependencias

```bash
pip install -r requirements.txt
```

### 4. Variables de entorno

Crea un archivo `.env` dentro de `backend/`:

```env
SUPABASE_URL=tu_url_de_supabase
SUPABASE_KEY=tu_anon_key
GEMINI_API_KEY=tu_clave_gemini
OPENWEATHER_API_KEY=tu_clave_weather
```

### 5. Ejecutar el servidor

```bash
uvicorn main:app --reload
```

El servidor estará disponible en `http://127.0.0.1:8000`

### 6. Configurar la conexión en Android Studio

En `Plant-Guardian/frontend/app/src/main/java/com/fiorella/plantguardian/data/network/RetrofitClient.kt`, ajusta `BASE_URL` según tu caso:

| Entorno | URL |
|---|---|
| Emulador Android | `http://10.0.2.2:8000/` |
| Dispositivo físico | `http://<tu-IP-local>:8000/` (ej: `192.168.1.X`) |

Para obtener tu IP local en Windows ejecuta `ipconfig`, en Mac/Linux `ifconfig` o `ip a`.