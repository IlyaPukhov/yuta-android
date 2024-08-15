package com.yuta.common.ui

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater

abstract class CancelableDialog(
    private val dialogLayout: Int,
    private val activity: Activity
) : AlertDialog(activity) {

    lateinit var dialog: AlertDialog

    open fun start() {
        val builder = Builder(activity)
        val inflater = LayoutInflater.from(activity)
        builder.setView(inflater.inflate(dialogLayout, null))

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        dialog.show()
    }
}
