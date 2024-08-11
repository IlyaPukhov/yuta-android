package com.yuta.common.util

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun hideKeyboard(activity: Activity, view: View? = null) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = view ?: activity.currentFocus
        focusedView?.let {
            imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            it.clearFocus()
        }
    }
}
