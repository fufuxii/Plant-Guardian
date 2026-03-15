import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime
from matplotlib import font_manager

font_path = r"E:\FIORELLA\UAB\sm_pr1\pr1_g3_2\Montserrat-Regular.ttf"
font_manager.fontManager.addfont(font_path)
plt.rcParams["font.family"] = "Montserrat"
plt.rcParams["font.size"] = 10

activities = [
    {"task": "F1-A1: Definición del sistema", "start": "2026-02-12", "end": "2026-02-22", "phase":"Fase 1"},
    {"task": "F1-A2: Análisis de requisitos", "start": "2026-02-23", "end": "2026-03-01", "phase":"Fase 1"},
    {"task": "F1-A3: Diseño del sistema", "start": "2026-03-02", "end": "2026-03-08", "phase":"Fase 1"},
    {"task": "F1-A4: Diseño UX/UI", "start": "2026-03-05", "end": "2026-03-08", "phase":"Fase 1"},
    {"task": "F2-A1: Desarrollo Backend", "start": "2026-03-09", "end": "2026-03-24", "phase":"Fase 2"},
    {"task": "F2-A2: Reconocimiento inteligente", "start": "2026-03-25", "end": "2026-04-12", "phase":"Fase 2"},
    {"task": "F2-A3: Gamificación", "start": "2026-04-05", "end": "2026-04-12", "phase":"Fase 2"},
    {"task": "F3-A1: Desarrollo frontend", "start": "2026-04-13", "end": "2026-05-01", "phase":"Fase 3"},
    {"task": "F3-A2: Integración de la app", "start": "2026-05-02", "end": "2026-05-24", "phase":"Fase 3"},
    {"task": "F4-A1: Pruebas de la app", "start": "2026-05-25", "end": "2026-06-14", "phase":"Fase 4"},
    {"task": "F4-A2: Documentos y presentación", "start": "2026-06-15", "end": "2026-07-18", "phase":"Fase 4"},
]

phase_colors = {
    "Fase 1": "#4C72B0", "Fase 2": "#55A868", 
    "Fase 3": "#C44E52", "Fase 4": "#8172B3"
}

fig, ax = plt.subplots(figsize=(14, 8), dpi=100)

for i, t in enumerate(activities):
    start = mdates.datestr2num(t["start"])
    end = mdates.datestr2num(t["end"])
    duration = end - start
    ax.barh(t["task"], duration, left=start, color=phase_colors[t["phase"]], 
        edgecolor="black", alpha=0.9, height=0.6, zorder=3)

ax.invert_yaxis()

ax.xaxis.set_major_locator(mdates.WeekdayLocator(byweekday=mdates.MO))
ax.xaxis.set_major_formatter(mdates.DateFormatter('%d %b'))
plt.xticks(rotation=45)

ax.grid(True, axis='x', color='#E0E0E0', linestyle='--', linewidth=0.8, zorder=1)
ax.grid(True, axis='y', color='#F5F5F5', linestyle='-', linewidth=0.5, zorder=1)

ax.spines['top'].set_visible(False)
ax.spines['right'].set_visible(False)
ax.spines['left'].set_color('#DDDDDD')
ax.spines['bottom'].set_color('#DDDDDD')

plt.title("Planificación de Plant Guardian", fontsize=16, pad=20, fontweight='bold', color='#333333')

plt.tight_layout()
plt.show()