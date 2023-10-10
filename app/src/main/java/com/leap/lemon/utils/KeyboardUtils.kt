package com.leap.lemon.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtils {
    fun showKeyboard(view: View?) {
        if (view == null) {
            return
        }
        try {
            view.requestFocus()
            if (view is EditText) {
                view.setSelection(view.text.length)
            }
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 1)
            (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, 0)
            if (isKeyboardShowed(view)) {
                return
            }
            inputMethodManager.toggleSoftInput(2, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showKeyboardDontChange(view: View?) {
        if (view == null) {
            return
        }
        try {
            view.requestFocus()
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 1)
            (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, 0)
            if (isKeyboardShowed(view)) {
                return
            }
            inputMethodManager.toggleSoftInput(2, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isKeyboardShowed(view: View?): Boolean {
        return if (view == null) {
            false
        } else (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isActive(
            view
        )
    }

    fun isKeyboardShowed(activity: Activity): Boolean {
        val editText =
            (if (activity.window.decorView.findFocus() is EditText) activity.window.decorView.findFocus() as EditText else null) ?: return false
        return (editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isActive(editText)
    }

    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        try {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyboard(activity: Activity) {
        try {
            activity.window.setSoftInputMode(3)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}