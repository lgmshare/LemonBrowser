package com.leap.lemon.utils

import com.leap.lemon.App

object UserConfig {


    var first_launch: Boolean
        get() = App.getInstance().sharePref.getBoolean("first_launch", true)
        set(value) {
            App.getInstance().sharePref.edit().putBoolean("first_launch", value).apply()
        }

    var country: String
        get() = App.getInstance().sharePref.getString("country", "") ?: ""
        set(str) {
            App.getInstance().sharePref.edit().putString("country", str).apply()
        }
}