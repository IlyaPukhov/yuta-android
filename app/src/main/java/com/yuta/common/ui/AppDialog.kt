package com.yuta.common.ui

import android.app.Activity
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

abstract class AppDialog(
    private val dialogLayout: Int,
    private val activity: Activity
) : AlertDialog(activity) {

    lateinit var dialog: AlertDialog

    open fun start() {
        val builder = Builder(activity)
        val inflater = LayoutInflater.from(activity)
        builder.setView(inflater.inflate(dialogLayout, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        dialog.show()
    }
}
