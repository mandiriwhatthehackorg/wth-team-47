/*
 * *
 *  * Created by Wellsen on 7/12/19 12:17 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 12:17 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.forgotpass

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityForgotPassBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ForgotPassActivity : BindingActivity<ActivityForgotPassBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_forgot_pass

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as ForgotPassViewModel
    vm.forgotPassFormState.observe(this@ForgotPassActivity, Observer {
      val forgotPassFormState = it ?: return@Observer

      binding.tilUsername.error = if (forgotPassFormState.usernameError == null) null
      else getString(forgotPassFormState.usernameError)

      // disable login button unless both username / password is valid
      binding.btnResetPass.isEnabled = forgotPassFormState.isDataValid
    })

    binding.etUsername.apply {
      afterTextChanged {
        vm.onForgotPassFormChanged(
          binding.etUsername.text.toString()
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

  }

}
