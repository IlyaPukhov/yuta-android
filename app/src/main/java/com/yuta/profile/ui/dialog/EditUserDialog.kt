package com.yuta.profile.ui.dialog

import android.util.Patterns
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.santalu.maskara.widget.MaskEditText
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.FieldUtils.trimmedText
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.User
import com.yuta.profile.viewmodel.UserDetailsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class EditUserDialog(
    private val fragment: Fragment,
    private val onEditSuccessCallback: () -> Unit
) : CancelableDialog(R.layout.dialog_edit_user, fragment.requireActivity()) {

    companion object {
        private const val PHONE_NUMBER_LENGTH = 10
        private const val VK_URL_REGEX = "(https?://)?(www\\.)?vk\\.com/(\\w|\\d|[._])+?/?"
    }

    private var isPhoneValid = true
    private var isEmailValid = true
    private var isVkValid = true

    private val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }
    private val closeButton: Button by lazy { dialog.findViewById(R.id.close) }
    private val biographyView: EditText by lazy { dialog.findViewById(R.id.biography) }
    private val phoneNumberView: MaskEditText by lazy { dialog.findViewById(R.id.phone_number) }
    private val emailView: EditText by lazy { dialog.findViewById(R.id.email) }
    private val vkView: EditText by lazy { dialog.findViewById(R.id.vk) }
    private val errorPhone: TextView by lazy { dialog.findViewById(R.id.error_phone) }
    private val errorEmail: TextView by lazy { dialog.findViewById(R.id.error_email) }
    private val errorVk: TextView by lazy { dialog.findViewById(R.id.error_vk) }

    private val detailsViewModel: UserDetailsViewModel by fragment.viewModels()

    override fun start() {
        super.start()

        fillFields(UserUtils.currentUser!!)
        setupInputFields()
        setupButtons()
    }

    private fun setupInputFields() {
        setupField(phoneNumberView, errorPhone)
        setupField(emailView, errorEmail)
        setupField(vkView, errorVk)
    }

    private fun editUserData() {
        detailsViewModel.viewModelScope.launch {
            val result = detailsViewModel.editDetails(
                UserUtils.getUserId(fragment.requireContext()),
                getData(biographyView),
                getData(phoneNumberView),
                getData(emailView),
                getData(vkView)
            ).first()
            handleEditDetailsResult(result)
        }
    }

    private fun handleEditDetailsResult(isSuccess: Boolean) {
        if (isSuccess) {
            onEditSuccessCallback()
            dismiss()
        }
    }

    private fun fillFields(user: User) {
        user.biography?.let { biographyView.setText(it) }
        user.phoneNumber?.let { phoneNumberView.setText(it) }
        user.vk?.let { vkView.setText(it) }
        user.eMail?.let { emailView.setText(it) }
    }

    private fun getData(editText: EditText): String? {
        return editText.trimmedText().takeIf { it.isNotEmpty() }
    }

    private fun setupField(editText: EditText, errorView: TextView) {
        setupEditTextValidation(editText, errorView)

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                showError(isFieldValid(editText), errorView)
            }
        }

        if (editText == vkView) {
            editText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    showError(isFieldValid(editText), errorView)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupButtons() {
        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), vkView)
            editUserData()
        }
        closeButton.setOnClickListener { dismiss() }
    }

    private fun isFieldValid(editText: EditText): Boolean {
        return when (editText) {
            phoneNumberView -> {
                val phoneLength = phoneNumberView.unMasked.length
                phoneLength == PHONE_NUMBER_LENGTH || phoneLength == 0
            }

            emailView -> {
                val email = getData(emailView)
                email == null || Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

            vkView -> {
                val vk = getData(vkView)
                vk == null || Pattern.compile(VK_URL_REGEX).matcher(vk).matches()
            }

            else -> false
        }
    }

    private fun showError(show: Boolean, errorView: TextView) {
        errorView.visibility = if (show) GONE else VISIBLE
    }

    private fun setupEditTextValidation(editText: EditText, errorView: TextView) {
        editText.doOnTextChanged { _, _, _, _ ->
            when (editText) {
                phoneNumberView -> {
                    val phoneLength = phoneNumberView.unMasked.length
                    isPhoneValid = phoneLength == PHONE_NUMBER_LENGTH || phoneLength == 0
                }

                emailView -> {
                    val email = getData(emailView)
                    isEmailValid = email == null || Patterns.EMAIL_ADDRESS.matcher(email).matches()
                }

                vkView -> {
                    val vk = getData(vkView)
                    isVkValid = vk == null || Pattern.compile(VK_URL_REGEX).matcher(vk).matches()
                }
            }
            showError(true, errorView)
            updateSubmitEnable()
        }
    }

    private fun updateSubmitEnable() {
        submitButton.isEnabled = isPhoneValid && isEmailValid && isVkValid
    }
}
