/*
 * *
 *  * Created by Wellsen on 7/12/19 3:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 3:54 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityLoginBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.register.RegisterActivity
import com.wellsen.mandiri.whatthehack.android.ui.resetpass.ResetPassActivity
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

      binding.btnLogin.isEnabled = loginFormState.isDataValid
    })

    vm.error.observe(this@LoginActivity, Observer {
      Toast.makeText(this@LoginActivity, it, Toast.LENGTH_LONG).show()
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
      startActivity(
        Intent(this, RegisterActivity::class.java),
        ActivityOptions.makeSceneTransitionAnimation(
          this,
          Pair.create(binding.ivLogo, "logo"),
          Pair.create(binding.tilUsername, "username"),
          Pair.create(binding.btnLogin, "button")
        ).toBundle()
      )
    }

    binding.btnForgotPass.setOnClickListener {
      startActivity(
        Intent(this, ResetPassActivity::class.java),
        ActivityOptions.makeSceneTransitionAnimation(
          this,
          Pair.create(binding.ivLogo, "logo"),
          Pair.create(binding.tilUsername, "username"),
          Pair.create(binding.btnLogin, "button")
        ).toBundle()
      )
    }

  }

}
