package com.leap.lemon.widget

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import android.webkit.SafeBrowsingResponse
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.leap.lemon.App
import com.leap.lemon.WebPagesManager
import com.leap.lemon.utils.FirebaseEventUtil


class MyWebView @JvmOverloads constructor(context: Context = App.getInstance(), attributeSet: AttributeSet? = null) : WebView(context, attributeSet) {

    enum class WebState {
        IDEA, LOADING, FINISH, STOPPED
    }

    init {
        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            useWideViewPort = false
            loadWithOverviewMode = true
            defaultTextEncodingName = "utf-8"
            loadsImagesAutomatically = true
            allowContentAccess = true
            allowFileAccess = true
            builtInZoomControls = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            setSupportZoom(false)
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        webChromeClient = MyWebChromeClient()
        webViewClient = MyWebClient()
    }

    private var loadState: WebState = WebState.IDEA

    val isIdea: Boolean
        get() {
            return loadState == WebState.IDEA
        }

    val isStopped: Boolean
        get() {
            return loadState == WebState.STOPPED
        }

    val isLoadingFinish: Boolean
        get() {
            return loadState == WebState.FINISH
        }

    val isLoading: Boolean
        get() {
            return loadState == WebState.LOADING
        }

    var startTimes = 0L
    var isClearHistory = false

    fun startLoad(path: String) {
        loadState = WebState.LOADING
        if (Patterns.WEB_URL.matcher(path).matches()) {
            loadUrl(path)
        } else {
            loadUrl("https://www.google.com/search?q=${path}")
        }
    }

    fun stopLoad() {
        loadState = WebState.STOPPED
        stopLoading()
    }

    fun clearWebHistory() {
        stopLoad()
        clearHistory()
        isClearHistory = true
    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (loadState != WebState.STOPPED) {
                if (isClearHistory) {
                    isClearHistory = false
                    view.clearHistory()
                }

                loadState = if (newProgress >= 100) {
                    if (startTimes != 0L) {
                        FirebaseEventUtil.loadEvent(((System.currentTimeMillis() - startTimes) / 1000))
                        startTimes = 0L
                    }

                    WebState.FINISH
                } else {
                    WebState.LOADING
                }
                WebPagesManager.webListener?.onProgressChanged(newProgress, this@MyWebView)
            }
        }
    }

    inner class MyWebClient : WebViewClient() {
        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loadState = WebState.LOADING
            startTimes = System.currentTimeMillis()
        }

        override fun onPageFinished(view: WebView?, url: String) {
            super.onPageFinished(view, url)
            if (loadState == WebState.LOADING) {
                loadState = WebState.FINISH
            }
        }

        @RequiresApi(Build.VERSION_CODES.O_MR1)
        override fun onSafeBrowsingHit(view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) {
            callback?.backToSafety(true)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }
    }
}
