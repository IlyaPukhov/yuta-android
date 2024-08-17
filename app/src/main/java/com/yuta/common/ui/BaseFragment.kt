package com.yuta.common.ui

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yuta.app.R
import com.yuta.authorization.ui.LogoutDialog

abstract class BaseFragment : Fragment() {

    private val progressLayout: View by lazy { requireView().findViewById(R.id.progressLayout) }
    protected val logoutButton: Button by lazy { requireView().findViewById(R.id.logout) }

    init {
        logoutButton.setOnClickListener { openLogoutDialog() }
    }

    fun showProgress(show: Boolean) {
        progressLayout.visibility = if (show) VISIBLE else GONE
    }

    private fun openLogoutDialog() {
        LogoutDialog(this).start()
    }
}