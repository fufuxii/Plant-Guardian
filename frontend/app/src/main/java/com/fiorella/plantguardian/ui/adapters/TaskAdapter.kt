package com.fiorella.plantguardian.ui.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.TaskData

class TaskAdapter(
    private val listaTareas: List<TaskData>,
    private val esHoy: Boolean
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreTarea)
        val cbEstado: CheckBox? = view.findViewById(R.id.cbTareaEstado)
        val tvFecha: TextView? = view.findViewById(R.id.tvFrecuenciaTarea)
    }

    override fun getItemCount(): Int = listaTareas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layout = if (esHoy) {
            R.layout.item_list_tasks_check
        } else {
            R.layout.item_list_tasks
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tarea = listaTareas[position]
        holder.tvNombre.text = tarea.titulo

        if (esHoy) {
            holder.cbEstado?.isChecked = tarea.hecho
        } else {
            holder.tvFecha?.text = tarea.fecha_proxima
        }
    }
}