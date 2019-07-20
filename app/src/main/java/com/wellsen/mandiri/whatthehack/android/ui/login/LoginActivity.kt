/*
 * *
 *  * Created by Wellsen on 7/20/19 7:16 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 7:07 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityLoginBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_GOOGLE_SIGN_IN
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SUBMIT_KTP
import com.wellsen.mandiri.whatthehack.android.ui.main.MainActivity
import com.wellsen.mandiri.whatthehack.android.ui.submitktp.SubmitKtpActivity
import com.wellsen.mandiri.whatthehack.android.util.EMAIL
import com.wellsen.mandiri.whatthehack.android.util.LOGGED_IN
import com.wellsen.mandiri.whatthehack.android.util.NAME
import com.wellsen.mandiri.whatthehack.android.util.PHOTO_URL
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class LoginActivity : BindingActivity<ActivityLoginBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_login

  private val sp: SharedPreferences by inject()

  private var googleSignInClient: GoogleApiClient? = null
  private var auth: FirebaseAuth? = null

  override fun onCreate(savedInstanceState: Bundle?) {

    // Configure Google Sign In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .requestProfile()
      .requestId()
      .build()

    googleSignInClient = GoogleApiClient.Builder(this)
      .enableAutoManage(this) {
        Timber.e(it.errorMessage)
        it.errorMessage?.let { it1 -> toastOnMainThread(it1) }
      }
      .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
      .build()

    auth = FirebaseAuth.getInstance()

    if (sp.getBoolean(LOGGED_IN, false)) {
      login()
      finish()
    }

    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as LoginViewModel

    vm.loginFormState.observe(this@LoginActivity, Observer {
      val loginFormState = it ?: return@Observer

      binding.tilUsername.error = if (loginFormState.usernameError == null) null
      else getString(loginFormState.usernameError)
      binding.tilPassword.error = if (loginFormState.passwordError == null) null
      else getString(loginFormState.passwordError)

      binding.btnLogin.isEnabled = loginFormState.isDataValid
    })

    vm.status.observe(this@LoginActivity, Observer {

      if (it.code == Status.ERROR) {
        Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_LONG).show()
      } else {
        // Proceed login
        Timber.d("Proceed login")
      }

    })

    binding.etUsername.afterTextChanged {
      vm.onLoginFormChanged(
        binding.etUsername.text.toString(),
        binding.etPassword.text.toString()
      )
    }

    binding.etPassword.apply {
      afterTextChanged {
        vm.onLoginFormChanged(
          binding.etUsername.text.toString(),
          binding.etPassword.text.toString()
        )
      }

      setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
          EditorInfo.IME_ACTION_DONE ->
            vm.onClick(null)
        }
        false
      }
    }

    binding.btnLogin.setOnClickListener {
      login()
    }

    binding.btnRegister.setOnClickListener {
      /*startActivity(
        Intent(this, RegisterActivity::class.java),
        ActivityOptions.makeSceneTransitionAnimation(
          this,
          Pair.create(binding.ivLogo, "logo"),
          Pair.create(binding.tilUsername, "username"),
          Pair.create(binding.btnLogin, "button")
        ).toBundle()
      )*/

//      startActivity(Intent(this, VideoCallActivity::class.java))
      val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleSignInClient)
      startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN)
    }

  }

  private fun getDetail(result: GoogleSignInResult) {
    if (result.isSuccess) {
      val signInAccount = result.signInAccount
      if (signInAccount != null) {
        sp.edit().putString(NAME, signInAccount.displayName).apply()
        sp.edit().putString(EMAIL, signInAccount.email).apply()
        sp.edit().putString(PHOTO_URL, signInAccount.photoUrl.toString()).apply()

        startActivityForResult(Intent(this, SubmitKtpActivity::class.java), REQUEST_SUBMIT_KTP)
      } else {
        Timber.e("Sign in data null")
      }
    } else {
      Timber.d("Masuk Dengan Google Gagal")
      Toast.makeText(this, "Masuk Dengan Google Gagal", Toast.LENGTH_LONG).show()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    when (requestCode) {
      // proceed
      REQUEST_SUBMIT_KTP -> {
        if (resultCode != Activity.RESULT_OK) {
          return
        }

        login()
      }

      REQUEST_GOOGLE_SIGN_IN -> getDetail(Auth.GoogleSignInApi.getSignInResultFromIntent(data))
    }
  }

  private fun login() {
    sp.edit().putBoolean(LOGGED_IN, true).apply()
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }

  override fun onStart() {
    super.onStart()
    if (googleSignInClient != null)
      googleSignInClient!!.connect()
  }

  override fun onStop() {
    super.onStop()
    if (googleSignInClient != null && googleSignInClient!!.isConnected) {
      logout()
      googleSignInClient!!.disconnect()
    }
  }

  fun logout() {
    if (googleSignInClient != null && googleSignInClient!!.isConnected)
      Auth.GoogleSignInApi.signOut(googleSignInClient).setResultCallback {}
  }

}
