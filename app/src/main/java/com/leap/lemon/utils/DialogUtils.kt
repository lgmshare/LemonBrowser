package com.leap.lemon.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.leap.idea.R

class DialogUtils {

    companion object {

        fun showCleanDialog(context: Context, addCategoryInterface: DialogInterface?) {
            val dialog = Dialog(context)
            val rootView: View = LayoutInflater.from(context).inflate(R.layout.dialog_clean, null)
            rootView.findViewById<View>(R.id.btn_confirm).setOnClickListener {
                addCategoryInterface?.confirm()
                dialog.dismiss()
            }
            rootView.findViewById<View>(R.id.btn_close).setOnClickListener { dialog.dismiss() }
            rootView.measure(0, 0)
            dialog.setContentView(rootView)
            val window = dialog.window
            val attributes = window?.attributes
            if (attributes != null) {
                attributes.x = 0
                attributes.y = 0
                attributes.width = ScreenUtils.getScreenWidth(context) - ScreenUtils.dpToPx(56)
            }

            if (window != null) {
                window.setBackgroundDrawableResource(R.color.translate)
                window.setGravity(Gravity.CENTER)
                window.attributes = attributes
            }
            try {
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}