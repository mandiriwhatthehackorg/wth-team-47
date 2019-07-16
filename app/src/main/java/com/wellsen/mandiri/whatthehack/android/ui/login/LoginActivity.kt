/*
 * *
 *  * Created by Wellsen on 7/16/19 5:22 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 4:12 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityLoginBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.submitktp.SubmitKtpActivity
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

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

      startActivity(Intent(this, SubmitKtpActivity::class.java))
    }

    binding.btnForgotPass.setOnClickListener {

      Timber.d("Not implemented yet")

    }

  }

}
