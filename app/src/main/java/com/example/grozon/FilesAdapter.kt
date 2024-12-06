package com.example.grozon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.BaseAdapter
import java.io.File

class FilesAdapter(
    private val context: Context,
    private val files: List<File>,
    private val clickListener: (File) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = files.size

    override fun getItem(position: Int): Any = files[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)

        val fileNameTextView: TextView = view.findViewById(R.id.pdfFileName)
        val openPdfButton: Button = view.findViewById(R.id.openPdfBtn)

        val file = files[position]
        fileNameTextView.text = file.name

        openPdfButton.setOnClickListener {
            clickListener(file)
        }

        return view
    }
}
