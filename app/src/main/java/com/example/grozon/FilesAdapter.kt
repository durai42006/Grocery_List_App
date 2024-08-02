package com.example.grozon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FilesAdapter(private val files: List<File>, private val clickListener: (File) -> Unit) : RecyclerView.Adapter<FilesAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView = itemView.findViewById(R.id.pdfFileName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.fileNameTextView.text = file.name
        holder.itemView.setOnClickListener {
            clickListener(file)
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }
}
