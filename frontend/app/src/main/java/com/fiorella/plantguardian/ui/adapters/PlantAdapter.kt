package com.fiorella.plantguardian.ui.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.model.PlantData

class PlantAdapter(
    private var listaPlantas: List<PlantData>,
    private val onPlantClick: (PlantData) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    class PlantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombrePlanta)
        val lugar: TextView = view.findViewById(R.id.tvLugarPlanta)
        val foto: ImageView = view.findViewById(R.id.ivPlanta)
        val cargador: ProgressBar = view.findViewById(R.id.pbCargandoImagen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_plants, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val planta = listaPlantas[position]

        holder.nombre.text = planta.nombre_comun
        holder.lugar.text = planta.lugar

        holder.foto.load(planta.imagen_url) {
            crossfade(true)
            error(R.drawable.ic_my_plants)
            listener(
                onStart = {
                    holder.cargador.visibility = View.VISIBLE
                },
                onSuccess = { _, _ ->
                    holder.cargador.visibility = View.GONE
                },
                onError = { _, _ ->
                    holder.cargador.visibility = View.GONE
                }
            )
        }

        holder.itemView.setOnClickListener {
            onPlantClick(planta)
        }
    }

    override fun getItemCount(): Int = listaPlantas.size

    fun actualizarLista(nuevaLista: List<PlantData>) {
        this.listaPlantas = nuevaLista
        notifyDataSetChanged()
    }
}