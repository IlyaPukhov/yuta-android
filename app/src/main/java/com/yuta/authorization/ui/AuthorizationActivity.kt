package com.yuta.authorization.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.viewModelScope
import com.yuta.app.MainActivity
import com.yuta.app.R
import com.yuta.authorization.viewmodel.AuthorizationViewModel
import com.yuta.common.util.FieldUtils.trimmedText
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.util.NetworkUtils
import kotlinx.coroutines.launch

class AuthorizationActivity : AppCompatActivity() {

    private val authViewModel: AuthorizationViewModel by viewModels()

    private val errorText: TextView by lazy { findViewById(R.id.error_text) }
    private val loginButton: Button by lazy { findViewById(R.id.login_button) }
    private val loginView: EditText by lazy { findViewById(R.id.login) }
    private val passwordView: EditText by lazy { findViewById(R.id.password) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_YUTA_Common)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        checkInitialConditions()
        setupEditViews()
        setupLoginButton()
    }

    private fun checkInitialConditions() {
        if (hasInternetConnection()) {
            if (UserUtils.getUserId(this) >= 0) {
                setupNetworkBaseUrl()
                openApp()
            }
        } else {
            openNetworkDialog()
        }
    }

    // TODO убрать в релизе
    private fun setupNetworkBaseUrl() {
        val ipv4 = UserUtils.getSharedPreferences(this).getString("ipv4", "")
        NetworkUtils.BASE_URL = ipv4.orEmpty()
    }

    private fun setupEditViews() {
        loginView.doOnTextChanged { _, _, _, _ -> updateLoginButtonState() }
        passwordView.doOnTextChanged { _, _, _, _ -> updateLoginButtonState() }
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            verifyLogin()
        }
    }

    private fun verifyLogin() {
        val login = loginView.trimmedText()
        val password = passwordView.trimmedText()

        setupNetworkBaseUrl()

        val loadingDialog = LoadingDialog(this)
        loadingDialog.start()

        authViewModel.viewModelScope.launch {
            authViewModel.auth(login, password).collect { userId ->
                if (userId >= 0) {
                    UserUtils.setUserId(this@AuthorizationActivity, userId)
                    openApp()
                } else {
                    errorText.visibility = VISIBLE
                }
                loadingDialog.dismiss()
            }
        }
    }

    private fun openApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun openNetworkDialog() {
        NetworkDialog(this).start()
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } == true
    }

    private fun updateLoginButtonState() {
        val loginText = loginView.trimmedText()
        val passwordText = passwordView.trimmedText()
        loginButton.isEnabled = loginText.isNotEmpty() && passwordText.isNotEmpty()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }
}
