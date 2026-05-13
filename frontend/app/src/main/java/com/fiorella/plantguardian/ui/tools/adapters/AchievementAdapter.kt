package com.fiorella.plantguardian.ui.tools.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.AchievementData

class AchievementAdapter(private var logros: List<AchievementData>) :
    RecyclerView.Adapter<AchievementAdapter.LogroViewHolder>() {

    private val posicionesAnimadas = mutableSetOf<Int>()

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
            crossfade(false)
            decoderFactory(SvgDecoder.Factory())
        }

        if (position !in posicionesAnimadas) {
            posicionesAnimadas.add(position)
            holder.itemView.alpha = 0f
            holder.itemView.translationY = 40f
            holder.itemView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(350)
                .setStartDelay(minOf(position, 5) * 80L)
                .setInterpolator(DecelerateInterpolator())
                .start()
        } else {
            holder.itemView.alpha = 1f
            holder.itemView.translationY = 0f
        }
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