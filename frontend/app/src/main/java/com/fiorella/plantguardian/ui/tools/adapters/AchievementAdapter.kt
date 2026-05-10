package com.fiorella.plantguardian.ui.tools.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.AchievementData

class AchievementAdapter(private val logros: List<AchievementData>) :
    RecyclerView.Adapter<AchievementAdapter.LogroViewHolder>() {

    class LogroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icono: ImageView = view.findViewById(R.id.ivIconoLogro)
    }

    override fun getItemCount() = logros.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement, parent, false)
        return LogroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogroViewHolder, position: Int) {
        val logro = logros[position]

        holder.icono.load(logro.icono) {
            crossfade(true)
            decoderFactory(SvgDecoder.Factory())
        }

        holder.itemView.alpha = 0f
        holder.itemView.scaleX = 0.8f
        holder.itemView.scaleY = 0.8f

        holder.itemView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(position * 60L)
            .setDuration(300)
            .start()
    }
}