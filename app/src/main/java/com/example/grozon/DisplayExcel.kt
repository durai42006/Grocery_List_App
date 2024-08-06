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
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class DisplayExcel : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CellAdapter
    private var numColumns: Int = 0
    private var workbook: Workbook? = null
    private var pdfFilePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display_excel, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val addRowBtn: Button = view.findViewById(R.id.addRowBtn)
        val addColumnBtn: Button = view.findViewById(R.id.addColumnBtn)
        val exportBtn: Button = view.findViewById(R.id.exportBtn)
        val shareBtn: Button = view.findViewById(R.id.shareBtn)

        // Get the workbook bytes from the arguments
        val workbookBytes = arguments?.getByteArray("workbook")
        workbookBytes?.let {
            workbook = WorkbookFactory.create(ByteArrayInputStream(it))
            displayExcelFile()
        }

        addRowBtn.setOnClickListener {
            adapter.addRow()
        }

        addColumnBtn.setOnClickListener {
            adapter.addColumn()
            numColumns++
            recyclerView.layoutManager = GridLayoutManager(context, numColumns)
        }

        exportBtn.setOnClickListener {
            workbook?.let {
                exportToPDF(it)
            }
        }

        shareBtn.setOnClickListener {
            pdfFilePath?.let { path ->
                sharePDF(path)
            }
        }

        return view
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

    private fun exportToPDF(workbook: Workbook) {
        val timestamp = System.currentTimeMillis().toString()
        val pdfFileName = "output_$timestamp.pdf"
        val appInternalPath = "${context?.getExternalFilesDir(null)?.absolutePath}/$pdfFileName"
        pdfFilePath = appInternalPath

        saveWorkbookAsPDF(workbook, appInternalPath)

        val externalPath = "${Environment.getExternalStorageDirectory().absolutePath}/Download/$pdfFileName"
        saveWorkbookAsPDF(workbook, externalPath)

        Toast.makeText(activity, "PDF exported to $appInternalPath and $externalPath", Toast.LENGTH_LONG).show()
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
        val uri: Uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share PDF using"))
    }
    companion object {
        @JvmStatic
        fun newInstance(workbookBytes: ByteArray): DisplayExcel {
            return DisplayExcel().apply {
                arguments = Bundle().apply {
                    putByteArray("workbook", workbookBytes)
                }
            }
        }
    }

}
