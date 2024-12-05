package com.example.grozon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class ExternalViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the file path from the intent
        val filePath = intent.getStringExtra("filePath")
        if (filePath != null) {
            val file = File(filePath)
            if (file.exists()) {
                // Check file extension and call the appropriate method
                when (file.extension.lowercase()) {
                    "pdf" -> openPdf(file)
                    "xlsx", "xls" -> openExcel(file)
                    else -> {
                        // Show a message if the file is not supported
                        Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to open a PDF file
    private fun openPdf(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }

    // Method to open an Excel file
    private fun openExcel(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            // Set MIME type for Excel files
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }
}
