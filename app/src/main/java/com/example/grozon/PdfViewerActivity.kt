package com.example.grozon

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class PdfViewerActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView: PDFView = findViewById(R.id.pdfView)
        val pdfFilePath = intent.getStringExtra("pdfFilePath")
        val pdfFile = File(pdfFilePath)

        pdfView.fromFile(pdfFile)
            .load()
    }
}
