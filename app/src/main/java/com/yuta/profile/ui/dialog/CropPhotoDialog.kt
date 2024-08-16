package com.yuta.profile.ui.dialog

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.canhub.cropper.CropImageView
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.UserUtils
import com.yuta.profile.viewmodel.UserDetailsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CropPhotoDialog(
    private val fragment: Fragment,
    private val onCropSuccessCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_crop_photo, fragment.requireActivity()) {

    private val cropImageView: CropImageView by lazy { dialog.findViewById(R.id.cropImageView) }
    private val closeButton: Button by lazy { dialog.findViewById(R.id.close) }
    private val saveButton: Button by lazy { dialog.findViewById(R.id.save_miniature) }

    private val detailsViewModel: UserDetailsViewModel by fragment.viewModels()

    override fun start() {
        super.start()

        loadImage(UserUtils.currentUser!!.photo, cropImageView)

        closeButton.setOnClickListener { dismiss() }
        saveButton.setOnClickListener { cropPhoto(UserUtils.getUserId(fragment.requireContext())) }
    }

    private fun cropPhoto(userId: Int) {
        val factWidth = cropImageView.wholeImageRect!!.width()
        val factHeight = cropImageView.wholeImageRect!!.height()
        val cropRect: Rect = cropImageView.cropRect!!

        detailsViewModel.viewModelScope.launch {
            val isSuccess = detailsViewModel.updateMiniature(
                userId = userId,
                ivWidth = factWidth,
                ivHeight = factHeight,
                croppedWidth = cropRect.width(),
                croppedHeight = cropRect.height(),
                offsetX = cropRect.left,
                offsetY = cropRect.top
            ).first()
            handleCropResult(isSuccess)
        }
    }

    private fun handleCropResult(isCropped: Boolean) {
        if (isCropped) {
            onCropSuccessCallback()
            dismiss()
        }
    }

    private fun loadImage(path: String, cropImageView: CropImageView) {
        GlideUtils.getConfiguredGlideBuilder(Glide.with(cropImageView).asBitmap().load(UserUtils.getPath(path)))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    cropImageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}
