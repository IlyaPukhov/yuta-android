package com.yuta.authorization.ui

import android.widget.Button
import androidx.fragment.app.Fragment
import com.yuta.app.R
import com.yuta.common.ui.InteractiveDialog
import com.yuta.common.util.UserUtils

class LogoutDialog(private val fragment: Fragment) : InteractiveDialog(R.layout.dialog_logout, fragment) {

    override fun start() {
        super.start()

        dialog.findViewById<Button>(R.id.close)?.setOnClickListener { view -> dismiss() }
        dialog.findViewById<Button>(R.id.submit)?.setOnClickListener { view ->
            UserUtils.logout(fragment.requireActivity())
            dismiss()
        }
    }
}
