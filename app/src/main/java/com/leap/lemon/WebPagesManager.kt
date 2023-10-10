package com.leap.lemon

import android.graphics.Bitmap
import com.leap.lemon.models.WebTab
import com.leap.lemon.widget.MyWebView

object WebPagesManager {

    val webPageLists = mutableListOf<WebTab>().apply {
        sortBy { it.createTime }
    }

    lateinit var currentWebTab: WebTab

    init {
        addNewWeb()
    }

    fun addNewWeb() {
        val webTab = WebTab(null, MyWebView(App.getInstance()))
        webPageLists.add(webTab)
        changeWeb(webTab)
    }

    fun changeWeb(webTab: WebTab) {
        currentWebTab = webTab
        webListener?.onWebChanged(currentWebTab)
    }

    fun remove(webTab: WebTab) {
        webPageLists.remove(webTab)
        currentWebTab = webPageLists.first()
        webListener?.onWebChanged(currentWebTab)
    }

    fun cleanAllWeb() {
        currentWebTab.webView.stopLoad()
        currentWebTab.webView.clearWebHistory()
        webPageLists.clear()
        webListener?.clean()
        addNewWeb()
    }

    fun updateCurrentWebTabBitmap(bitMap: Bitmap?) {
        if (bitMap != null) {
            currentWebTab.bitmap = bitMap
        }
    }

    fun startLoad(url: String) {
        currentWebTab.inputText = url
        currentWebTab.webView.startLoad(url)
    }

    fun stopLoad() {
        currentWebTab.webView.stopLoad()
    }

    fun getTabCount(): Int {
        return webPageLists.size
    }

    fun isCurrentWebView(webView: MyWebView): Boolean {
        return currentWebTab.webView == webView
    }

    var webListener: WebListener? = null

    interface WebListener {
        fun onProgressChanged(progress: Int, view: MyWebView)
        fun onWebChanged(webTab: WebTab)
        fun clean() {}
    }
}