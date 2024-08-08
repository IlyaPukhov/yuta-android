package com.ilyap.yuta.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var lastItemId: Int = -1
    private var lastPress: Long = -1
    private var isReadonlyProfile = false

    fun handleBackPress(
        currentTime: Long,
        onSelectPreviousNavTab: () -> Unit,
        onPopBackStack: () -> Unit,
        onExit: () -> Unit,
        showToast: (String) -> Unit
    ) {
        when {
            lastItemId >= 0 -> onSelectPreviousNavTab()
            isReadonlyProfile -> {
                onPopBackStack()
                isReadonlyProfile = false
            }

            else -> {
                if (currentTime - lastPress > 5000L) {
                    showToast("Press back again to exit")
                    lastPress = currentTime
                } else {
                    onExit()
                }
            }
        }
    }
}