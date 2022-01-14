package com.edpub.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.progressindicator.LinearProgressIndicator

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val wbMyWebView = findViewById<WebView>(R.id.wbMyWebView)
        val lpiWebPageProgress = findViewById<LinearProgressIndicator>(R.id.lpiWebPageProgress)

        wbMyWebView.settings.javaScriptEnabled = true

        val url = intent.getStringExtra("URL")
        wbMyWebView.webViewClient = object : WebViewClient(){}
        wbMyWebView.loadUrl(url!!)
        lpiWebPageProgress.progress = 0
        wbMyWebView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                //lpiWebPageProgress.progress=newProgress
                super.onProgressChanged(view, newProgress)
                if(newProgress==100){
                    lpiWebPageProgress.visibility = View.INVISIBLE
                }
            }
        }
    }
}