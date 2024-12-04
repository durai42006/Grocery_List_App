import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.grozon.R
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.ByteArrayOutputStream

class importExcel : Fragment() {

    private val REQUEST_CODE_IMPORT = 1
    private val REQUEST_CODE_PERMISSIONS = 2
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//        val importBtn: Button = view.findViewById(R.id.importbtn)
        importExcelFile()
//        importBtn.setOnClickListener {
//
//        }

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
                val workbook = WorkbookFactory.create(inputStream)
                inputStream?.close()

                val outputStream = ByteArrayOutputStream()
                workbook.write(outputStream)
                val workbookBytes = outputStream.toByteArray()

                // Extract file name or file path
                val filePath = uri.path ?: "ImportedFile.xlsx"

                // Correctly create the DisplayExcel fragment with filePath
                val fragment = DisplayExcel.newInstance(workbookBytes, filePath)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()

                Toast.makeText(activity, "Excel file imported", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Failed to import Excel file", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { it1 -> ContextCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
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
