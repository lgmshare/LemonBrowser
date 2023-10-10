package com.leap.lemon

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.leap.lemon.ui.FullscreenActivity
import java.lang.ref.WeakReference

class App : Application(), LifecycleEventObserver {

    companion object {
        private lateinit var INSTANCE: App

        fun getInstance(): App {
            return INSTANCE
        }

    }

    //应用当前activity
    private var currentActivity: WeakReference<Activity>? = null
    lateinit var sharePref: SharedPreferences

    //应用是否处于后台状态
    var runningForeground = false

    var activityCount = 0

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        sharePref = getSharedPreferences("lemon_browser", MODE_PRIVATE)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                if (runningForeground) {
                    return
                }

                runningForeground = true
                val activity = currentActivity?.get()
                if (activity != null && activity !is FullscreenActivity) {
                    startActivity(Intent(this, FullscreenActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }
            }

            Lifecycle.Event.ON_STOP -> {
                if (!runningForeground) {
                    return
                }
                runningForeground = false
            }

            else -> {

            }
        }
    }

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityCount++
        }

        override fun onActivityStarted(activity: Activity) {
            currentActivity = WeakReference(activity)
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            activityCount--
        }
    }
}