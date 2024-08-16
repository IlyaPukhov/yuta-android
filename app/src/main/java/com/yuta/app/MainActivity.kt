package com.yuta.app

import android.graphics.PixelFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yuta.app.viewmodel.MainViewModel
import com.yuta.common.callback.NavigationToProfileCallback
import com.yuta.common.util.UserUtils

class MainActivity : AppCompatActivity(), NavigationToProfileCallback {

    private val viewModel: MainViewModel by viewModels()
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val bottomNavigationView: BottomNavigationView by lazy { findViewById(R.id.bottomNavigationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupWithNavController(bottomNavigationView, navController)
        initializeBackPressedDispatcher()
    }

    private fun initializeBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this) {
            viewModel.handleBackPress(
                onSelectPreviousNavTab = { selectPreviousNavTab() },
                onPopBackStack = { navController.popBackStack() },
                onExit = { finish() },
                showToast = { Toast.makeText(baseContext, getString(R.string.back_press), Toast.LENGTH_LONG).show() }
            )
        }
    }

    private fun navigate(action: Int, bundle: Bundle? = null) {
        navController.navigate(action, bundle)
    }

    private fun selectPreviousNavTab() {
        viewModel.setLastItemId(bottomNavigationView.selectedItemId)
        bottomNavigationView.selectedItemId = viewModel.getLastItemId()
        viewModel.resetLastItemId()
    }

    private fun selectNavTab(id: Int) {
        viewModel.setLastItemId(bottomNavigationView.selectedItemId)
        bottomNavigationView.selectedItemId = id
    }

    override fun navigate(userId: Int) {
        if (UserUtils.getUserId(this) == userId) {
            this.selectNavTab(R.id.profileFragment)
        } else {
            val bundle = Bundle().apply { putInt("userId", userId) }
            this.navigate(R.id.action_searchFragment_to_readonlyProfileFragment, bundle)
            viewModel.setReadonly(true)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }
}
