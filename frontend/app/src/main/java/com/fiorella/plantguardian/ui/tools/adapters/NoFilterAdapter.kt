package com.fiorella.plantguardian.ui.tools.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class NoFilterAdapter(context: Context, layout: Int, val items: Array<String>) :
    ArrayAdapter<String>(context, layout, items) {

    override fun getFilter(): Filter {
        return object : Filter() {
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