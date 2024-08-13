package com.yuta.common.ui

import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.fragment.app.Fragment

abstract class InteractiveDialog(
    private val dialogLayout: Int, fragment: Fragment
) : AppDialog(dialogLayout, fragment.requireActivity()) {

    private val activity = fragment.requireActivity()

    override fun start() {
        val builder = Builder(activity)
        val inflater = LayoutInflater.from(activity)
        builder.setView(inflater.inflate(dialogLayout, null))

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        dialog.show()
    }
}
