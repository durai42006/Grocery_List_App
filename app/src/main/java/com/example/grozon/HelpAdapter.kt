package com.example.grozon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter

class HelpAdapter(
    private val context: Context,
    private val items: List<String>,
    private val contents: Map<String, String>
) : BaseAdapter() {

    private val expandedPositions = mutableSetOf<Int>() // Track which items are expanded

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_help, parent, false)

        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)

        // Set title
        val title = items[position]
        titleTextView.text = title

        // Set content visibility based on expansion state
        if (expandedPositions.contains(position)) {
            contentTextView.visibility = View.VISIBLE
            contentTextView.text = contents[title]
        } else {
            contentTextView.visibility = View.GONE
        }

        return view
    }

    fun toggleContentVisibility(position: Int) {
        if (expandedPositions.contains(position)) {
            expandedPositions.remove(position)
        } else {
            expandedPositions.add(position)
        }
        notifyDataSetChanged() // Refresh the ListView
    }
}
