package com.example.grozon

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class PdfViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfWebView: WebView = findViewById(R.id.pdfWebView)

        val pdfFile = intent.getSerializableExtra("pdfFile") as File
        val pdfUrl = "https://docs.google.com/gview?embedded=true&url=${pdfFile.toURI()}"

        pdfWebView.settings.javaScriptEnabled = true
        pdfWebView.webViewClient = WebViewClient()
        pdfWebView.loadUrl(pdfUrl)
    }
}
