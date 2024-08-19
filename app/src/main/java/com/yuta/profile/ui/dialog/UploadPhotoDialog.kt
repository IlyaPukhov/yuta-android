package com.yuta.profile.ui.dialog

import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.FileUtils
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.User
import com.yuta.profile.viewmodel.UserDetailsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UploadPhotoDialog(
    private val fragment: Fragment,
    private val onUploadSuccessCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_upload_photo, fragment.requireActivity()) {

    private val imageView: ImageView by lazy { dialog.findViewById(R.id.photo) }
    private val closeButton: ImageView by lazy { dialog.findViewById(R.id.close) }
    private val deleteButton: ImageView by lazy { dialog.findViewById(R.id.delete_photo) }
    private val cropButton: ImageView by lazy { dialog.findViewById(R.id.pick_miniature) }
    private val pickPhotoButton: ImageView by lazy { dialog.findViewById(R.id.pick_photo) }

    private var selectedImageUri: Uri? = null

    private val detailsViewModel: UserDetailsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        val user = UserUtils.currentUser ?: return

        GlideUtils.loadImageToImageViewWithoutCaching(imageView, user.croppedPhoto)
        setupButtons(user)
    }

    private fun setupButtons(user: User) {
        closeButton.setOnClickListener { dismiss() }
        deleteButton.setOnClickListener { GlideUtils.loadImageToImageViewWithoutCaching(imageView, user.photo) }
        cropButton.setOnClickListener {
            if (user.photo == PhotoMenuDialog.DEFAULT_USER_PHOTO && selectedImageUri == null) return@setOnClickListener
            if (selectedImageUri != null) {
                updatePhoto(UserUtils.getUserId(fragment.requireContext()))
            } else {
                onUploadSuccessCallback()
            }
        }
        pickPhotoButton.setOnClickListener { imagePickerLauncher.launch("image/*") }
    }

    private fun updatePhoto(userId: Int) {
        val inputStream = fragment.requireContext().contentResolver.openInputStream(selectedImageUri ?: return)
        val filename = FileUtils.getFilename(context, selectedImageUri)

        if (inputStream == null || filename.isNullOrBlank()) return

        detailsViewModel.viewModelScope.launch {
            val isUploadSuccess = detailsViewModel.uploadPhoto(userId, filename.toString(), inputStream).first()
            handleUpdateSuccess(isUploadSuccess)
        }
    }

    private fun handleUpdateSuccess(isUploadSuccess: Boolean) {
        if (isUploadSuccess) {
            onUploadSuccessCallback()
            dismiss()
        }
    }

    private fun setImage(uri: Uri) {
        selectedImageUri = uri
        imageView.setImageURI(selectedImageUri)
    }

    private val imagePickerLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { setImage(it) }
        }
}
