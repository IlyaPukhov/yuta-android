package com.yuta.authorization.ui

import android.app.Activity
import com.yuta.common.ui.CustomDialog

class NetworkDialog(activity: Activity) : CustomDialog(activity) {

    init {
        dialogLayout = R.layout.dialog_network
    }
}