package com.leap.lemon

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class App : Application(), LifecycleEventObserver {

    companion object {
        private lateinit var INSTANCE: App

        fun getInstance(): App {
            return INSTANCE
        }

    }

    lateinit var sharePref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        sharePref = getSharedPreferences("lemon_browser", MODE_PRIVATE)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> {

            }

            Lifecycle.Event.ON_START -> {

            }

            else -> {

            }
        }
    }
}