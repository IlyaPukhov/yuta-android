package com.yuta.common.util

import android.widget.TextView

object FieldUtils {

    fun TextView.trimmedText(): String = this.text.toString().trim()

    fun TextView.getData(): String? {
        return this.trimmedText().takeIf { it.isNotEmpty() }
    }
}
