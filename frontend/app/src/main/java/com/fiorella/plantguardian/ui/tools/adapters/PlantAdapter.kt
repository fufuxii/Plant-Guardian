package com.fiorella.plantguardian.ui.tools.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.PlantData

class PlantAdapter(
    private var listaPlantas: List<PlantData>,
    private val onPlantClick: (PlantData) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    private val posicionesAnimadas = mutableSetOf<Int>()

    class PlantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombrePlanta)
        val nombreCientifico: TextView = view.findViewById(R.id.tvNombreCientifico)
        val lugar: TextView = view.findViewById(R.id.tvLugarPlanta)
        val foto: ImageView = view.findViewById(R.id.ivPlanta)
        val cargador: ProgressBar = view.findViewById(R.id.pbCargandoImagen)
    }

    override fun getItemCount(): Int = listaPlantas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_plants, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val planta = listaPlantas[position]

        holder.nombre.text = planta.nombre_comun
        holder.nombreCientifico.text = planta.nombre_cientifico
        holder.lugar.text = planta.lugar

        if (holder.foto.tag != planta.imagen_url) {
            holder.foto.tag = planta.imagen_url
            holder.foto.load(planta.imagen_url) {
                crossfade(false)
                error(R.drawable.ic_my_plants)
                listener(
                    onStart = { holder.cargador.visibility = View.VISIBLE },
                    onSuccess = { _, _ -> holder.cargador.visibility = View.GONE },
                    onError = { _, _ -> holder.cargador.visibility = View.GONE }
                )
            }
        }

        holder.itemView.setOnClickListener { onPlantClick(planta) }

        if (position !in posicionesAnimadas) {
            posicionesAnimadas.add(position)
            holder.itemView.alpha = 0f
            holder.itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(minOf(position, 5) * 60L)
                .start()
        } else {
            holder.itemView.alpha = 1f
        }
    }

    fun actualizarLista(nuevaLista: List<PlantData>) {
        this.listaPlantas = nuevaLista
        notifyDataSetChanged()
    }
}