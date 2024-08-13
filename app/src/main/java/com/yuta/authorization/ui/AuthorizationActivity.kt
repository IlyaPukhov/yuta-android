package com.yuta.authorization.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.yuta.app.MainActivity
import com.yuta.app.R
import com.yuta.authorization.viewmodel.AuthorizationViewModel
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.util.NetworkUtils
import kotlinx.coroutines.launch

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var errorText: TextView
    private lateinit var loginButton: Button
    private lateinit var loginView: EditText
    private lateinit var passwordView: EditText
    private val authViewModel: AuthorizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_YUTA_Common)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        initializeViews()
        checkInitialConditions()
        setupTextWatchers()
        setupLoginButton()
    }

    private fun initializeViews() {
        loginView = findViewById(R.id.login)
        passwordView = findViewById(R.id.password)
        errorText = findViewById(R.id.error_text)
        loginButton = findViewById(R.id.login_button)
    }

    private fun checkInitialConditions() {
        if (hasInternetConnection() && UserUtils.getUserId(this) >= 0) {
            // TODO убрать в релизе
            val ipv4 = UserUtils.getSharedPreferences(this).getString("ipv4", "")
            NetworkUtils.BASE_URL = ipv4.toString()
            openApp()
        } else if (!hasInternetConnection()) {
            openNetworkDialog()
        }
    }

    private fun setupTextWatchers() {
        loginView.addTextChangedListener(authorizationTextWatcher())
        passwordView.addTextChangedListener(authorizationTextWatcher())
    }

    private fun authorizationTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            errorText.visibility = GONE
            updateLoginButtonState()
        }
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            verifyLogin()
        }
    }

    private fun verifyLogin() {
        val login = loginView.text.toString()
        val password = passwordView.text.toString()

        // TODO убрать в релизе
        val editor = UserUtils.getSharedPreferences(this).edit()
        editor.putString("ipv4", findViewById<EditText>(R.id.ipv4).text.toString()).apply()
        NetworkUtils.BASE_URL = UserUtils.getSharedPreferences(this).getString("ipv4", "").toString()

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
        val networkDialog = NetworkDialog(this)
        networkDialog.start()
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
        val loginText = loginView.text.toString().trim()
        val passwordText = passwordView.text.toString().trim()
        loginButton.isEnabled = loginText.isNotEmpty() && passwordText.isNotEmpty()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setFormat(PixelFormat.RGBA_8888)
    }
}
