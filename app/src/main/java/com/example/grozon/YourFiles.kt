package com.example.grozon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class YourFiles : Fragment() {

    private lateinit var pdfRecyclerView: RecyclerView
    private lateinit var excelRecyclerView: RecyclerView
    private lateinit var pdfAdapter: FilesAdapter
    private lateinit var excelAdapter: FilesAdapter
    private lateinit var pdfFiles: List<File>
    private lateinit var excelFiles: List<File>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_your_files, container, false)


//        val dividerItemDecoration = DividerItemDecoration(
//            context,
//            RecyclerView.VERTICAL
//        ).apply {
//            val drawable = context?.let { ContextCompat.getDrawable(it, R.drawable.) }
//            drawable?.let { setDrawable(it) }
//        }


        // Initialize RecyclerViews
        pdfRecyclerView = view.findViewById(R.id.pdfRecyclerView)
        excelRecyclerView = view.findViewById(R.id.excelRecyclerView)

//        pdfRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)



        pdfRecyclerView.layoutManager = LinearLayoutManager(context)
        excelRecyclerView.layoutManager = LinearLayoutManager(context)



//        pdfRecyclerView.addItemDecoration(dividerItemDecoration)
//        pdfRecyclerView.addItemDecoration(dividerItemDecoration)

        // Load PDF and Excel files
        loadPdfFiles()
        loadExcelFiles()

        // Create adapters and set them to RecyclerViews
        pdfAdapter = FilesAdapter(pdfFiles) { file -> openFile(file) }
        excelAdapter = FilesAdapter(excelFiles) { file -> openFile(file) }

        pdfRecyclerView.adapter = pdfAdapter
        excelRecyclerView.adapter = excelAdapter

        return view
    }


    private fun loadPdfFiles() {
        val appStorageDir = context?.getExternalFilesDir(null)
        pdfFiles = appStorageDir?.listFiles { _, name -> name.endsWith(".pdf") }?.toList() ?: emptyList()
    }

    private fun loadExcelFiles() {
        val appStorageDir = context?.getExternalFilesDir(null)
        excelFiles = appStorageDir?.listFiles { _, name -> name.endsWith(".xlsx") || name.endsWith(".xls") }?.toList() ?: emptyList()
    }

    private fun openFile(file: File) {
        val intent = Intent(context, ExternalViewerActivity::class.java)
        intent.putExtra("filePath", file.absolutePath)
        startActivity(intent)
    }
}