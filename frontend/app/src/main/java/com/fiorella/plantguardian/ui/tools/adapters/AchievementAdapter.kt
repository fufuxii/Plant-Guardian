package com.fiorella.plantguardian.ui.tools.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.AchievementData

class AchievementAdapter(private var logros: List<AchievementData>) :
    RecyclerView.Adapter<AchievementAdapter.LogroViewHolder>() {

    class LogroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icono: ImageView = view.findViewById(R.id.ivIconoLogro)
    }

    fun actualizarLista(nuevaLista: List<AchievementData>) {
        val diffCallback = AchievementDiffCallback(this.logros, nuevaLista)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.logros = nuevaLista
        diffResult.dispatchUpdatesTo(this)
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
            .setStartDelay(position * 30L)
            .setDuration(300)
            .start()
    }

    class AchievementDiffCallback(
        private val oldList: List<AchievementData>,
        private val newList: List<AchievementData>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos] == newList[newPos]
    }
}