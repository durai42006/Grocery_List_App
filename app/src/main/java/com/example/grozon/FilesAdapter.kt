package com.example.grozon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FilesAdapter(private val files: List<File>, private val clickListener: (File) -> Unit) : RecyclerView.Adapter<FilesAdapter.FileViewHolder>() {

    // List of predefined background colors (customize the colors here)
    private val colors = listOf(
        R.color.color1, // Light Red
        R.color.color2, // Light Green
        R.color.color3  // Light Blue
    )

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView = itemView.findViewById(R.id.pdfFileName)
        val openPdfButton: Button = itemView.findViewById(R.id.openPdfBtn)
        val itemContainer: View = itemView.findViewById(R.id.itemContainer)  // Root layout container for changing background
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.fileNameTextView.text = file.name

        // Retrieve the background color from the predefined list
        val backgroundColor = ContextCompat.getColor(holder.itemView.context, colors[position % colors.size])

        // Set the background color for each item in the list
        holder.itemContainer.setBackgroundColor(backgroundColor)

        // Set click listener for opening the PDF file
        holder.openPdfButton.setOnClickListener {
            clickListener(file)
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }
}

