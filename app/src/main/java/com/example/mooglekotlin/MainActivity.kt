package com.example.webViewkotlin

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import com.example.mooglekotlin.FirebaseAuth.LoginActivity
import com.example.mooglekotlin.R
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var home: ImageButton
    private lateinit var chat: ImageButton
    private lateinit var undo: ImageButton
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        init()
        goWebView()
        click()

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun init(){
        // val - constant, var - is not
        home = findViewById(R.id.btn_home)
        chat = findViewById(R.id.btn_profile)
        undo = findViewById(R.id.btn_undo)
        webView= findViewById(R.id.webView)
        progressBar = findViewById(R.id.prgb)

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true

    }

    fun goWebView() {
        webView.webViewClient = object: WebViewClient(){
            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true

            }
        }
        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress

                if(newProgress == 100){
                   progressBar.visibility = View.GONE
                }
               else{
                   progressBar.visibility = View.VISIBLE
               }
                btnUndoVisibility()
            }
        }
        webView.loadUrl("https://www.google.com")
    }

    fun click(){
        home.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                webView.loadUrl("https://www.google.com")
            }

        })

        undo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(webView.canGoBack()) {
                    webView.goBack()
                }
            }
        })

        chat.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                try {

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

        })

    }

    fun btnUndoVisibility(){
        undo.visibility = View.INVISIBLE
        if(webView.canGoBack()){
            undo.visibility = View.VISIBLE
        }
    }

}
