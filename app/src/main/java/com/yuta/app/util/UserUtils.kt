package com.yuta.app.util

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import com.yuta.__old.ui.LoginActivity
import com.yuta.domain.model.User
import com.yuta.domain.util.NetworkUtils

object UserUtils {

    private var currentUser: User? = null
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("session", MODE_PRIVATE)
    }

    fun getPath(path: String): String {
        return NetworkUtils.getBaseUrl() + path
    }

    fun setUserId(context: Context, id: Int) {
        if (id >= 0) {
            val sharedPreferences = getSharedPreferences(context)
            with(sharedPreferences.edit()) {
                putInt("user_id", id)
                apply()
            }
        }
    }

    fun getUserId(context: Context): Int {
        return getSharedPreferences(context).getInt("user_id", -1)
    }

    fun logOut(activity: Activity) {
        val sharedPreferences = getSharedPreferences(activity)
        with(sharedPreferences.edit()) {
            remove("user_id")
            apply()
        }

        val intent = Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        activity.startActivity(intent)
        activity.finish()
    }

    fun setCurrentUser(currentUser: User) {
        UserUtils.currentUser = currentUser
    }
}
