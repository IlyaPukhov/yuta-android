package com.yuta.profile.ui.dialog

import android.view.View.GONE
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.UserUtils

class PhotoMenuDialog(
    private val fragment: Fragment,
    private val onUpdateCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_photo_menu, fragment.requireActivity()) {

    companion object {
        const val DEFAULT_USER_PHOTO = "default-user-photo"
    }

    private val updatePhotoButton: Button by lazy { dialog.findViewById(R.id.update_photo) }
    private val editMiniatureButton: Button by lazy { dialog.findViewById(R.id.edit_miniature) }
    private val deletePhotoButton: Button by lazy { dialog.findViewById(R.id.delete_photo) }

    override fun start() {
        super.start()

        updatePhotoButton.setOnClickListener {
            openUpdatePhotoDialog()
            dismiss()
        }

        val userPhoto = UserUtils.currentUser!!.photo
        if (userPhoto.contains(DEFAULT_USER_PHOTO, ignoreCase = true)) {
            editMiniatureButton.visibility = GONE
            deletePhotoButton.visibility = GONE
        } else {
            editMiniatureButton.setOnClickListener {
                openCropPhotoDialog()
                dismiss()
            }
            deletePhotoButton.setOnClickListener {
                openDeletePhotoDialog()
                dismiss()
            }
        }
    }

    private fun openUpdatePhotoDialog() = UploadPhotoDialog(fragment) { openCropPhotoDialog() }.start()

    private fun openCropPhotoDialog() = CropPhotoDialog(fragment) { onUpdateCallback() }.start()

    private fun openDeletePhotoDialog() = DeletePhotoDialog(fragment) { onUpdateCallback() }.start()
}
