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

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        onBackPressedDispatcherInitialize()
    }

    private fun setupNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottomNavigationView!!, navController)
    }

    private fun onBackPressedDispatcherInitialize() {
        onBackPressedDispatcher.addCallback(this) {
            viewModel.handleBackPress(
                onSelectPreviousNavTab = { selectPreviousNavTab() },
                onPopBackStack = { navController.popBackStack() },
                onExit = { finish() },
                showToast = { Toast.makeText(baseContext, getString(R.string.back_press), Toast.LENGTH_LONG).show() }
            )
        }
    }

    fun navigate(action: Int, bundle: Bundle? = null) {
        navController.navigate(action, bundle)
    }

    private fun selectPreviousNavTab() {
        val currentItemId = bottomNavigationView?.selectedItemId ?: return
        viewModel.setLastItemId(currentItemId)
        bottomNavigationView?.selectedItemId = viewModel.getLastItemId()
        viewModel.resetLastItemId()
    }

    fun selectNavTab(id: Int) {
        val currentItemId = bottomNavigationView?.selectedItemId ?: return
        viewModel.setLastItemId(currentItemId)
        bottomNavigationView?.selectedItemId = id
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }
}
