package com.example.grozon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import java.io.File

class YourFiles : Fragment() {

    private lateinit var pdfListView: ListView
    private lateinit var excelListView: ListView
    private lateinit var pdfAdapter: FilesAdapter
    private lateinit var excelAdapter: FilesAdapter
    private lateinit var pdfFiles: List<File>
    private lateinit var excelFiles: List<File>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_your_files, container, false)

        // Initialize ListViews
        pdfListView = view.findViewById(R.id.pdfListView)
        excelListView = view.findViewById(R.id.excelListView)

        // Load files
        loadPdfFiles()
        loadExcelFiles()

        // Set adapters for ListViews
        pdfAdapter = FilesAdapter(requireContext(), pdfFiles) { file -> openFile(file) }
        excelAdapter = FilesAdapter(requireContext(), excelFiles) { file -> openFile(file) }

        pdfListView.adapter = pdfAdapter
        excelListView.adapter = excelAdapter

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
