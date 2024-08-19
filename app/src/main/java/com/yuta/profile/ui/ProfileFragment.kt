package com.yuta.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.yuta.app.R
import com.yuta.common.ui.BaseFragment
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.User
import com.yuta.domain.model.UserDto
import com.yuta.profile.ui.dialog.EditUserDialog
import com.yuta.profile.ui.dialog.PhotoMenuDialog
import com.yuta.profile.ui.dialog.SyncUserDialog
import com.yuta.profile.viewmodel.UserViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class ProfileFragment : BaseFragment() {

    protected val syncButton: Button by lazy { requireView().findViewById(R.id.logout) }
    protected val editDetailsButton: Button by lazy { requireView().findViewById(R.id.logout) }
    private val avatarImageView: ImageView by lazy { requireView().findViewById(R.id.photo) }
    private val contactsContainer: View by lazy { requireView().findViewById(R.id.contacts_container) }

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false).also {
            setupViews()
            updateProfile()
        }
    }

    private fun setupViews() {
        syncButton.setOnClickListener { openSyncDialog() }
        editDetailsButton.setOnClickListener { openEditUserDialog() }
        avatarImageView.setOnClickListener { openPhotoMenu() }
    }

    private fun updateProfile() {
        updateProfile(UserUtils.getUserId(requireActivity()))
    }

    protected fun updateProfile(userId: Int) {
        showProgress(true)
        avatarImageView.isEnabled = false

        userViewModel.viewModelScope.launch {
            val user = userViewModel.getProfile(userId).first()
            handleProfileResult(userId, user)
        }
    }

    private fun handleProfileResult(id: Int, user: User) {
        updateImage(user)
        fillViews(id, user)
        showProgress(false)
        avatarImageView.isEnabled = true
        UserUtils.currentUser = user
    }

    private fun fillViews(id: Int, user: User) {
        val fullName = UserUtils.getFullName(UserDto.fromUser(id, user))
        val faculty = "${getString(R.string.faculty)}: ${user.faculty}"
        val direction = "${getString(R.string.direction)}: ${user.direction}"
        val group = "${getString(R.string.group)}: ${user.group}"
        val projectsCount = "${user.doneProjectsCount}/${user.allProjectsCount}"
        val tasksCount = "${user.doneTasksCount}/${user.allTasksCount}"

        setDataInTextView(R.id.name, fullName)
        setDataInTextView(R.id.age, user.age)
        setDataInTextView(R.id.biography, user.biography)
        setDataInTextView(R.id.faculty, faculty)
        setDataInTextView(R.id.direction, direction)
        setDataInTextView(R.id.group, group)
        setDataInTextView(R.id.projects_count, projectsCount)
        setDataInTextView(R.id.tasks_count, tasksCount)
        setDataInTextView(R.id.teams_count, user.teamsCount.toString())
        setDataInTextView(R.id.phone_number, user.phoneNumber)
        setDataInTextView(R.id.email, user.eMail)
        setDataInTextView(R.id.vk, user.vk)

        contactsContainerVisibility(R.id.phone_number, R.id.email, R.id.vk)
    }

    private fun updateImage(user: User) {
        GlideUtils.loadImageToImageViewWithoutCaching(avatarImageView, user.croppedPhoto)
    }

    private fun contactsContainerVisibility(vararg fields: Int) {
        val isEmpty = fields.none { requireView().findViewById<View>(it).visibility == VISIBLE }
        contactsContainer.visibility = if (isEmpty) GONE else VISIBLE
    }

    private fun openSyncDialog() = SyncUserDialog(this) { updateProfile() }.start()

    private fun openEditUserDialog() = EditUserDialog(this) { updateProfile() }.start()

    private fun openPhotoMenu() = PhotoMenuDialog(this) { updateProfile() }.start()

    private fun setDataInTextView(id: Int, text: String?) {
        requireView().findViewById<TextView>(id).apply {
            visibility = if (text != null) {
                this.text = text
                VISIBLE
            } else GONE
        }
    }

}
