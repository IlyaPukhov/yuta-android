package com.yuta.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import com.yuta.app.R

class ReadonlyProfileFragment : ProfileFragment() {

    private val backButton: Button by lazy { requireView().findViewById(R.id.back_button) }

    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false).also {
            userId = arguments?.getInt("userId", -1) ?: return it
            updateProfile(userId)

            hideUnnecessaryButtons()
            setupBackButton()
        }
    }

    private fun hideUnnecessaryButtons() {
        logoutButton.visibility = GONE
        syncButton.visibility = GONE
        editDetailsButton.visibility = GONE
    }

    private fun setupBackButton() {
        backButton.visibility = VISIBLE
        backButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }
}
