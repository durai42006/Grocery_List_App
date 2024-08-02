package com.example.grozon

import CellAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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

class importExcel : Fragment() {

    private val REQUEST_CODE_IMPORT = 1
    private val REQUEST_CODE_PERMISSIONS = 2
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var workbook: Workbook? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CellAdapter
    private var numColumns: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
        }

        if (!allPermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_import_excel, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        val importBtn: Button = view.findViewById(R.id.importbtn)
        val exportBtn: Button = view.findViewById(R.id.exportbtn)
        val addRowBtn: Button = view.findViewById(R.id.addRowBtn)
        val addColumnBtn: Button = view.findViewById(R.id.addColumnBtn)

        importBtn.setOnClickListener {
            importExcelFile()
        }

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

        return view
    }

    private fun importExcelFile() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }
        startActivityForResult(intent, REQUEST_CODE_IMPORT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMPORT && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val inputStream = activity?.contentResolver?.openInputStream(uri)
                workbook = WorkbookFactory.create(inputStream)
                inputStream?.close()
                displayExcelFile()
                Toast.makeText(activity, "Excel file imported", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Failed to import Excel file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayExcelFile() {
        workbook?.let { workbook ->
            val sheet = workbook.getSheetAt(0)
            numColumns = (sheet.getRow(0)?.lastCellNum ?: 0 ).toInt()// Determine the number of columns

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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { it1 -> ContextCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
    }

    private fun getExternalStorageDirectory(): File? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Environment.getExternalStorageDirectory()
        } else {
            null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(activity, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            importExcel().apply {
                arguments = Bundle().apply {
                    // Add arguments if needed
                }
            }
    }
}
