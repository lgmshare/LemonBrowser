package com.leap.lemon.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.leap.idea.BuildConfig
import com.leap.idea.R
import com.leap.lemon.LEMON_DEBUG
import com.leap.lemon.exts.toast

class LemonUtils {
    companion object {

        fun log(msg: String?) {
            if (LEMON_DEBUG) {
                if (!msg.isNullOrEmpty()) {
                    Log.d("LemonLog", msg)
                }
            }
        }

        /**
         * 调用系统分享
         */
        fun jumpShare(context: Context, shareText: String?, shareTitle: String = "") {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareText ?: "")
                context.startActivity(Intent.createChooser(intent, shareTitle ?: ""))
            } catch (e: Exception) {
            }
        }

        fun jumpGooglePlayStore(context: Context) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    intent.data = Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}");
                    if (intent.resolveActivity(context.packageManager) == null) {
                        context.startActivity(intent)
                    } else {
                        context.toast("You don't have an app market installed, not even a browser!")
                    }
                }
            } catch (e: Exception) {
            }
        }

        fun copyToClipboard(context: Context, text: String) {
            val clip = ClipData.newPlainText(context.getString(R.string.app_name), text)
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
        }
    }
}