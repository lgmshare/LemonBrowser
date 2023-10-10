package com.leap.lemon.models

import android.graphics.Bitmap
import com.leap.lemon.widget.MyWebView

data class WebTab(var bitmap: Bitmap?, val webView: MyWebView) {

    val createTime = System.currentTimeMillis()

    var inputText: String? = null
}