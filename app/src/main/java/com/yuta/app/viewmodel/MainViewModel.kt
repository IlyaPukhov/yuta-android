package com.yuta.app.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var isReadonly = false
    private var lastItemId: Int = -1
    private var lastPress: Long = -1

    fun handleBackPress(
        currentTime: Long = System.currentTimeMillis(),
        onSelectPreviousNavTab: () -> Unit,
        onPopBackStack: () -> Unit,
        onExit: () -> Unit,
        showToast: () -> Unit
    ) {
        when {
            lastItemId >= 0 -> onSelectPreviousNavTab()
            isReadonly -> {
                onPopBackStack()
                isReadonly = false
            }

            else -> {
                if (currentTime - lastPress > 5000L) {
                    showToast()
                    lastPress = currentTime
                } else {
                    onExit()
                }
            }
        }
    }

    fun setLastItemId(itemId: Int) {
        lastItemId = itemId
    }

    fun getLastItemId(): Int {
        return lastItemId
    }

    fun resetLastItemId() {
        lastItemId = -1
    }

    fun setReadonly(readonly: Boolean) {
        isReadonly = readonly
    }
}
