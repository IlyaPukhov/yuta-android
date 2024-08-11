package com.yuta.common.ui

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.fragment.app.Fragment

abstract class CustomInteractiveDialog(fragment: Fragment) : CustomDialog(fragment.requireActivity()) {

    private val activity = fragment.requireActivity()

    override fun start() {
        val builder = Builder(activity)
        val inflater = LayoutInflater.from(activity)
        builder.setView(inflater.inflate(dialogLayout, null))

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()
    }
}
