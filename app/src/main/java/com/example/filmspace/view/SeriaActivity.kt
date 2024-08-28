package com.example.filmspace.view

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.filmspace.R
import com.example.filmspace.model.DoramaSeria
import com.example.filmspace.databinding.ActivitySeriaBinding

class SeriaActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySeriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySeriaBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val doramaSeria = intent.getSerializableExtra("doramaSeria") as? DoramaSeria
        if (doramaSeria != null) {
            val url = doramaSeria.dorama.listSeries[doramaSeria.seria]

            val webView = findViewById<WebView>(R.id.webView)
            webView.settings.javaScriptEnabled = true // Enable JavaScript
            webView.settings.domStorageEnabled = true // Enable DOM storage
            webView.webViewClient = WebViewClient() // Set WebViewClient to handle page navigation
            webView.webChromeClient = WebChromeClient() // Set WebChromeClient to enable fullscreen

            val html = """
                <html>
                    <body style="margin: 0; padding: 0;">
                        <iframe width="100%" height="100%" src="$url"
                            frameBorder="0" allow="clipboard-write; autoplay; fullscreen" 
                            webkitAllowFullScreen mozallowfullscreen allowFullScreen>
                        </iframe>
                    </body>
                </html>
            """.trimIndent()

            webView.loadData(html, "text/html", "UTF-8")
        } else {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
    }
}
