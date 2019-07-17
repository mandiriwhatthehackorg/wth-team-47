/*
 * *
 *  * Created by Wellsen on 7/17/19 2:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 1:56 PM
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
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityLoginBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SUBMIT_KTP
import com.wellsen.mandiri.whatthehack.android.ui.main.MainActivity
import com.wellsen.mandiri.whatthehack.android.ui.submitktp.SubmitKtpActivity
import com.wellsen.mandiri.whatthehack.android.util.LOGGED_IN
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class LoginActivity : BindingActivity<ActivityLoginBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_login

  private val sp: SharedPreferences by inject()

  override fun onCreate(savedInstanceState: Bundle?) {

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

      startActivityForResult(Intent(this, SubmitKtpActivity::class.java), REQUEST_SUBMIT_KTP)
    }

    binding.btnForgotPass.setOnClickListener {

      Timber.d("Not implemented yet")

    }

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    if (requestCode == REQUEST_SUBMIT_KTP) {
      // proceed
      login()
    }
  }

  private fun login() {
    startActivity(Intent(this, MainActivity::class.java))
  }

}
