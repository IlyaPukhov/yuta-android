package com.yuta.common.ui

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

abstract class CustomDialog(
    private val dialogLayout: Int,
    private val activity: Activity
) : AlertDialog(activity) {

    lateinit var dialog: AlertDialog

    open fun start() {
        val builder = Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater

        builder.setView(inflater.inflate(dialogLayout, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
