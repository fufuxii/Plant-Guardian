package com.fiorella.plantguardian.ui.adapters
import android.widget.ArrayAdapter

class NoFilterAdapter(context: android.content.Context, layout: Int, val items: Array<String>) :
    ArrayAdapter<String>(context, layout, items) {

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.values = items
                results.count = items.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}