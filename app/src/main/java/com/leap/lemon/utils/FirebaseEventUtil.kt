package com.leap.lemon.utils

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FirebaseEventUtil {

    companion object {

        fun event(tag: String, params: Bundle? = null) {
            if (params == null) {
                LemonUtils.log("firebase打点:$tag")
            } else {
                LemonUtils.log("firebase打点:$tag ${params.toString()}")
            }
            Firebase.analytics.logEvent(tag, params)
        }

        fun userProperty(value: String) {
            LemonUtils.log("firebase属性打点 lemon_pe:$value")
            Firebase.analytics.setUserProperty("lemon_pe", value)
        }

        fun searchEvent(text: String) {
            event("lemon_search", Bundle().apply {
                putString("bro", text)
            })
        }

        fun newLinkEvent(text: String) {
            event("lemon_guid", Bundle().apply {
                putString("bro", text)
            })
        }

        fun newTabEvent(text: String) {
            event("lemon_newTab", Bundle().apply {
                putString("bro", text)
            })
        }

        fun loadEvent(time: Long) {
            event("lemon_reload", Bundle().apply {
                putLong("bro", if (time > 1) time else 1)
            })
        }

        fun cleanEvent(time: Long) {
            event("lemon_clean_toast", Bundle().apply {
                putLong("bro", if (time > 1) time else 1)
            })
        }
    }
}