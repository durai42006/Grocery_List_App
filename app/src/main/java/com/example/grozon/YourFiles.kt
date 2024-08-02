package com.example.grozon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class YourFiles : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PdfFileAdapter
    private lateinit var pdfFiles: List<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_your_files, container, false)

        recyclerView = view.findViewById(R.id.filesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadPdfFiles()
        adapter = PdfFileAdapter(pdfFiles)
        recyclerView.adapter = adapter

        return view
    }

    private fun loadPdfFiles() {
        val appStorageDir = context?.getExternalFilesDir(null)
        pdfFiles = appStorageDir?.listFiles { _, name -> name.endsWith(".pdf") }?.toList() ?: emptyList()
    }

    inner class PdfFileAdapter(private val pdfFiles: List<File>) :
        RecyclerView.Adapter<PdfFileAdapter.PdfFileViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfFileViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
            return PdfFileViewHolder(view)
        }

        override fun onBindViewHolder(holder: PdfFileViewHolder, position: Int) {
            val pdfFile = pdfFiles[position]
            holder.pdfFileName.text = pdfFile.name
            holder.openPdfBtn.setOnClickListener {
                openPdfFile(pdfFile)
            }
        }

        override fun getItemCount(): Int {
            return pdfFiles.size
        }

        inner class PdfFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val pdfFileName: TextView = itemView.findViewById(R.id.pdfFileName)
            val openPdfBtn: Button = itemView.findViewById(R.id.openPdfBtn)
        }
    }

    private fun openPdfFile(pdfFile: File) {
        val intent = Intent(context, PdfViewerActivity::class.java)
        intent.putExtra("pdfFile", pdfFile)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YourFiles().apply {
                arguments = Bundle().apply {
                    // Add arguments if needed
                }
            }
    }
}
