import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grozon.R
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.xmp.XMPDateTimeFactory.getCurrentDateTime
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class DisplayExcel : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CellAdapter
    private var numColumns: Int = 0
    private var workbook: Workbook? = null
    private val REQUEST_CODE_SAVE = 2

    private var pdfFilePath: String? = null

    @SuppressLint("MissingInflatedId", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display_excel, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val addRowBtn: Button = view.findViewById(R.id.addRowBtn)
        val saveBtn: Button = view.findViewById(R.id.saveBtn)
        val shareBtn: Button = view.findViewById(R.id.shareBtn)

        // Get the workbook bytes and file path from the arguments
        val workbookBytes = arguments?.getByteArray("workbook")
        val originalFilePath = arguments?.getString("filePath")
        workbookBytes?.let {
            workbook = WorkbookFactory.create(ByteArrayInputStream(it))
            displayExcelFile()
        }

        addRowBtn.setOnClickListener {
            adapter.addRow()
        }

        saveBtn.setOnClickListener {
            workbook?.let { updatedWorkbook ->
                // Save dynamically to user-selected location
                val timestamp = System.currentTimeMillis()
                val timeOnly = SimpleDateFormat("dd-MM HH:mm:ss").format(Date(timestamp))
                val pdfFileName = "Xl $timeOnly.xlsx"
                val defaultFileName = originalFilePath?.substringAfterLast("/") ?: "${pdfFileName}.xlsx"

                val saveIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    putExtra(Intent.EXTRA_TITLE, defaultFileName)
                }
                startActivityForResult(saveIntent, REQUEST_CODE_SAVE)

                // Save a uniquely named copy to app storage
                saveToAppStorage("${pdfFileName}", updatedWorkbook)
            } ?: Toast.makeText(context, "Workbook is not loaded.", Toast.LENGTH_SHORT).show()
        }


        shareBtn.setOnClickListener{
            workbook?.let {
                exportToPDF(it)
            }
        }

        return view
    }


    private fun saveToAppStorage(fileName: String, workbook: Workbook): String? {
        // Ensure the file has a .xlsx extension
        val sanitizedFileName = if (fileName.endsWith(".xlsx")) fileName else "$fileName.xlsx"
        val appInternalFile = File(requireContext().getExternalFilesDir(null), sanitizedFileName)

        return try {
            FileOutputStream(appInternalFile).use { outputStream ->
                workbook.write(outputStream) // Save workbook in Excel format
            }
            Toast.makeText(context, "File saved to app storage: ${appInternalFile.absolutePath}", Toast.LENGTH_SHORT).show()
            appInternalFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save file to app storage.", Toast.LENGTH_SHORT).show()
            null
        }
    }




    @SuppressLint("SimpleDateFormat")
    private fun exportToPDF(workbook: Workbook) {
        val timestamp = System.currentTimeMillis()
        val timeOnly = SimpleDateFormat("dd-MM HH:mm:ss").format(Date(timestamp))
        val pdfFileName = "Pdf $timeOnly.pdf"
        val appInternalPath = "${context?.getExternalFilesDir(null)?.absolutePath}/$pdfFileName"
        pdfFilePath = appInternalPath

        saveWorkbookAsPDF(workbook, appInternalPath)

        val externalPath = "${Environment.getExternalStorageDirectory().absolutePath}/Download/$pdfFileName"
        saveWorkbookAsPDF(workbook, externalPath)

        sharePDF(pdfFilePath!!)

        //Toast.makeText(activity, "PDF exported to $appInternalPath and $externalPath", Toast.LENGTH_LONG).show()
    }


    private fun saveWorkbookAsPDF(workbook: Workbook, filePath: String) {
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


    private fun sharePDF(filePath: String) {
        val file = File(filePath)
        val uri: Uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share PDF using"))
    }




        private fun displayExcelFile() {
        workbook?.let { workbook ->
            val sheet = workbook.getSheetAt(0)
            numColumns = (sheet.getRow(0)?.lastCellNum ?: 0).toInt()

            adapter = CellAdapter(sheet, numColumns)
            recyclerView.layoutManager = GridLayoutManager(context, numColumns)
            recyclerView.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SAVE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                try {
                    requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                        workbook?.write(outputStream)
                    }
                    Toast.makeText(context, "File saved successfully!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save the file.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Failed to retrieve file path.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(workbookBytes: ByteArray, filePath: String): DisplayExcel {
            return DisplayExcel().apply {
                arguments = Bundle().apply {
                    putByteArray("workbook", workbookBytes)
                    putString("filePath", filePath)
                }
            }
        }
    }
}
