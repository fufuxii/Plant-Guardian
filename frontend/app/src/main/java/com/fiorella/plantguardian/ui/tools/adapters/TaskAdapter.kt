package com.fiorella.plantguardian.ui.tools.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.TaskData

class TaskAdapter(
    private val tareas: List<TaskData>,
    private val esHoy: Boolean,
    private val onTaskCompleted: (TaskData) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val check: CheckBox? = view.findViewById(R.id.cbTareaEstado)
        val texto: TextView = view.findViewById(R.id.tvNombreTarea)
        val tvFecha: TextView? = view.findViewById(R.id.tvFrecuenciaTarea)
    }

    override fun getItemCount(): Int = tareas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layout = if (esHoy) R.layout.item_list_tasks_check else R.layout.item_list_tasks
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.texto.text = tarea.titulo

        if (holder.check != null) {
            holder.check.isChecked = tarea.hecho
            holder.check.isEnabled = !tarea.hecho
            holder.texto.alpha = if (tarea.hecho) 0.5f else 1.0f

            holder.check.setOnClickListener {
                if (holder.check.isChecked) {
                    holder.check.isChecked = false
                    onTaskCompleted(tarea)
                }
            }
        } else {
            holder.tvFecha?.text = formatearFecha(tarea.fecha_proxima)
            holder.texto.alpha = 1.0f
        }
    }

    private fun formatearFecha(fechaRaw: String): String {
        return try {
            val inputFecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val outputFecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val fecha = inputFecha.parse(fechaRaw)
            outputFecha.format(fecha!!)
        } catch (e: Exception) {
            fechaRaw
        }
    }
}