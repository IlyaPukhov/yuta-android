package com.yuta.common.util

import android.widget.EditText

object FieldUtils {

    fun EditText.trimmedText(): String = this.text.toString().trim()
}
