package com.example.grozon

import CellAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileOutputStream

class DisplayExcel : Fragment() {

    private var fileUri: Uri? = null
    private var workbook: Workbook? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CellAdapter
    private var numColumns: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fileUri = Uri.parse(it.getString("fileUri"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display_excel, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val exportBtn: Button = view.findViewById(R.id.exportbtn)
        val addRowBtn: Button = view.findViewById(R.id.addRowBtn)
        val addColumnBtn: Button = view.findViewById(R.id.addColumnBtn)

        exportBtn.setOnClickListener {
            workbook?.let {
                exportToPDF(it)
            }
        }

        addRowBtn.setOnClickListener {
            adapter.addRow()
        }

        addColumnBtn.setOnClickListener {
            adapter.addColumn()
            numColumns++ // Update the number of columns in the fragment
            recyclerView.layoutManager = GridLayoutManager(context, numColumns) // Update the layout manager to the new column count
        }

        fileUri?.let {
            loadWorkbook(it)
            displayExcelFile()
        }

        return view
    }

    private fun loadWorkbook(uri: Uri) {
        val inputStream = activity?.contentResolver?.openInputStream(uri)
        workbook = WorkbookFactory.create(inputStream)
        inputStream?.close()
    }

    private fun displayExcelFile() {
        workbook?.let { workbook ->
            val sheet = workbook.getSheetAt(0)
            numColumns = (sheet.getRow(0)?.lastCellNum ?: 0 ).toInt() // Determine the number of columns

            adapter = CellAdapter(sheet, numColumns)
            recyclerView.layoutManager = GridLayoutManager(context, numColumns)
            recyclerView.adapter = adapter
        }
    }

    private fun exportToPDF(workbook: Workbook) {
        val timestamp = System.currentTimeMillis().toString()
        val pdfFileName = "output_$timestamp.pdf"

        val appInternalPath = context?.getExternalFilesDir(null)?.absolutePath + "/$pdfFileName"
        saveWorkbookAsPDF(workbook, appInternalPath)

        val externalPath = getExternalStorageDirectory()?.absolutePath + "/Download/$pdfFileName"
        saveWorkbookAsPDF(workbook, externalPath)

        Toast.makeText(activity, "PDF exported to $appInternalPath and $externalPath", Toast.LENGTH_LONG).show()
    }

    private fun saveWorkbookAsPDF(workbook: Workbook, filePath: String?) {
        if (filePath == null) return
        val pdfWriter = PdfWriter(FileOutputStream(filePath))
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val sheet = workbook.getSheetAt(0)
        val numColumns = (sheet.getRow(0)?.lastCellNum ?: 0).toInt()

        val table = Table(FloatArray(numColumns) { 1f })

        // Add header row
        val headerRow = sheet.getRow(0)
        for (i in 0 until numColumns) {
            val cell = headerRow?.getCell(i)
            table.addCell(cell?.toString() ?: "Header $i")
        }

        // Add remaining rows
        for (rowIndex in 1 until sheet.physicalNumberOfRows) {
            val row = sheet.getRow(rowIndex)
            for (i in 0 until numColumns) {
                val cell = row.getCell(i)
                table.addCell(cell?.toString() ?: "")
            }
        }

        document.add(table)
        document.close()
        pdfDocument.close()
    }

    private fun getExternalStorageDirectory(): File? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Environment.getExternalStorageDirectory()
        } else {
            null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DisplayExcel().apply {
                arguments = Bundle().apply {
                    // Add arguments if needed
                }
            }
    }
}
