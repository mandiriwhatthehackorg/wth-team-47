/*
 * *
 *  * Created by Wellsen on 7/12/19 12:17 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 11:59 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityLoginBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.forgotpass.ForgotPassActivity
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginActivity : BindingActivity<ActivityLoginBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_login

  override fun onCreate(savedInstanceState: Bundle?) {
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

      // disable login button unless both username / password is valid
      binding.btnLogin.isEnabled = loginFormState.isDataValid
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

    }

    binding.btnForgotPass.setOnClickListener {
      startActivity(
        Intent(this, ForgotPassActivity::class.java),
        ActivityOptions.makeSceneTransitionAnimation(
          this,
          binding.ivLogo, "logo"
        ).toBundle()
      )
    }

  }

}
