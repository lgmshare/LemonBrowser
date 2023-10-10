package com.leap.lemon.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class SimpleActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


}