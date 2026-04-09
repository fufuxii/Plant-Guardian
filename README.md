# 🌿 Plant Guardian

**Plant Guardian** es una aplicación móvil diseñada para el cuidado inteligente de plantas, combinando Inteligencia Artificial (IA) y elementos de gamificación para transformar la jardinería en una experiencia interactiva y educativa.

---

## Características Principales
- **Análisis de la planta:** Integración con **Google Gemini AI** para diagnosticar la salud de las plantas a través de fotos.
- **Identificación botánica:** Uso de la API de **Pl@ntNet** para reconocer especies automáticamente.
- **Sistema de gamificación:** 
  - Progresión por niveles y puntos de experiencia (XP).
  - Sistema de logros (Nivel, Colección de Plantas, Constancia en Tareas).
  - Avatares desbloqueables según el nivel del usuario.
- **Gestión de tareas:** Calendario dinámico de riego y cuidados basado en el clima local con **OpenWeather**.

---

## Tecnología

### Backend
- **Lenguaje:** Python 3.10+
- **Framework:** [FastAPI](https://fastapi.tiangolo.com/)
- **Base de Datos:** [Supabase](https://supabase.com/) (PostgreSQL)
- **Autenticación:** JWT & Bcrypt
- **Arquitectura:** Arquitectura por capas

### Frontend
- **Lenguaje:** Kotlin
- **Entorno:** Android Studio
- **Arquitectura:** MVVM (Model-View-ViewModel)

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
└── .gitignore         # Archivos excluidos de Git