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

    private var navController: NavController? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        onBackPressedDispatcherInitialize()
    }

    private fun setupNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottomNavigationView!!, navController!!)
    }

    private fun onBackPressedDispatcherInitialize() {
        onBackPressedDispatcher.addCallback(this) {
            viewModel.handleBackPress(
                currentTime = System.currentTimeMillis(),
                onSelectPreviousNavTab = { selectPreviousNavTab() },
                onPopBackStack = { navController!!.popBackStack() },
                onExit = { finish() },
                showToast = { message -> Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show() }
            )
        }
    }

    private fun selectPreviousNavTab() {
        bottomNavigationView!!.selectedItemId = viewModel.lastItemId
        viewModel.lastItemId = -1
    }

    fun selectNavTab(id: Int) {
        viewModel.lastItemId = bottomNavigationView!!.selectedItemId
        bottomNavigationView!!.selectedItemId = id
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }
}
