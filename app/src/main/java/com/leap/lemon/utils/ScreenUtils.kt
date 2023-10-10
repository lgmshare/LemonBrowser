package com.leap.lemon.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.roundToInt

class ScreenUtils {

    companion object {

        fun getScreenWidth(context: Context): Int {
            val displayMetrics = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun dpToPx(i: Int): Int {
            return (Resources.getSystem().displayMetrics.density * i).roundToInt()
        }
    }

}