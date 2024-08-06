package com.ilyap.yutarefactor

import android.graphics.PixelFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilyap.yuta.R
import com.ilyap.yutarefactor.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
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
        setupWithNavController(bottomNavigationView, navController)
    }

    private fun onBackPressedDispatcherInitialize() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = System.currentTimeMillis()
                viewModel.handleBackPress(
                    currentTime = currentTime,
                    onSelectPreviousNavTab = { selectPreviousNavTab() },
                    onPopBackStack = { navController.popBackStack() },
                    onExit = { finish() },
                    showToast = { message -> Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show() }
                )
            }
        })
    }

    private fun selectPreviousNavTab() {
        bottomNavigationView.selectedItemId = viewModel.lastItemId
        viewModel.lastItemId = -1
    }

    fun selectNavTab(id: Int) {
        viewModel.lastItemId = bottomNavigationView.selectedItemId
        bottomNavigationView.selectedItemId = id
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }
}
