package com.yuta.profile.ui.dialog

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.profile.ui.ProfileFragment
import com.yuta.profile.viewmodel.UserDetailsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SyncUserDialog(
    private val fragment: ProfileFragment,
    private val onProfileUpdateCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_sync_user, fragment.requireActivity()) {

    private val errorText: TextView by lazy { dialog.findViewById(R.id.error_text) }
    private val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }
    private val closeButton: EditText by lazy { dialog.findViewById(R.id.close) }
    private val password: EditText by lazy { dialog.findViewById(R.id.submit_password) }

    private val detailsViewModel: UserDetailsViewModel by fragment.viewModels()

    override fun start() {
        super.start()

        setupEditText(password)

        closeButton.setOnClickListener { dismiss() }
        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), password)
            syncUserData(password.text.toString())
        }
    }

    private fun syncUserData(password: String) {
        errorText.visibility = GONE

        detailsViewModel.viewModelScope.launch {
            val isSynchronized =
                detailsViewModel.syncProfile(UserUtils.getUserId(fragment.requireActivity()), password).first()
            handleSyncResult(isSynchronized)
        }
    }

    private fun handleSyncResult(isSynchronized: Boolean) {
        if (isSynchronized) {
            onProfileUpdateCallback()
            dismiss()
        }
        errorText.visibility = VISIBLE
    }

    private fun setupEditText(editText: EditText) {
        editText.doOnTextChanged { text, _, _, _ ->
            errorText.visibility = GONE
            submitButton.isEnabled = !text.isNullOrBlank()
        }
    }
}
