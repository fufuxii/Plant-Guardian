package com.fiorella.plantguardian.ui.tools.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.fiorella.plantguardian.R
import com.fiorella.plantguardian.data.schemas.IconData
import com.google.android.material.imageview.ShapeableImageView

class IconAdapter(
    private val iconos: List<IconData>,
    private val onIconSelected: (String) -> Unit
) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    private var selectedPosition = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcono: ShapeableImageView = view.findViewById(R.id.ivIconoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = iconos[position].url

        holder.ivIcono.load(url) {
            crossfade(true)
            placeholder(R.drawable.ic_user)
            error(R.drawable.ic_user)
            transformations(CircleCropTransformation())
        }

        holder.ivIcono.strokeWidth = if (selectedPosition == position) 8f else 0f
        holder.ivIcono.strokeColor = ColorStateList.valueOf(Color.parseColor("#638C65"))

        holder.itemView.setOnClickListener {
            val previous = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previous)
            notifyItemChanged(selectedPosition)
            onIconSelected(url)
        }
    }

    override fun getItemCount() = iconos.size
}