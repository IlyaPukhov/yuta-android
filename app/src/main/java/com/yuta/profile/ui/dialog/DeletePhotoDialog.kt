package com.yuta.profile.ui.dialog

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.UserUtils
import com.yuta.profile.viewmodel.UserDetailsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeletePhotoDialog(
    private val fragment: Fragment,
    private val onDeleteSuccessCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_delete, fragment.requireActivity()) {

    private val closeButton: Button by lazy { dialog.findViewById(R.id.close) }
    private val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }

    private val detailsViewModel: UserDetailsViewModel by fragment.viewModels()

    override fun start() {
        super.start()

        closeButton.setOnClickListener { dismiss() }
        submitButton.setOnClickListener { deletePhoto() }
    }

    private fun deletePhoto() {
        detailsViewModel.viewModelScope.launch {
            val isDeleted = detailsViewModel.deletePhoto(UserUtils.getUserId(fragment.requireContext())).first()
            handleDeleteResult(isDeleted)
        }
    }

    private fun handleDeleteResult(isDeleted: Boolean) {
        if (isDeleted) {
            onDeleteSuccessCallback()
            dismiss()
        }
    }
}
