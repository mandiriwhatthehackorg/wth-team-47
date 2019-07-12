/*
 * *
 *  * Created by Wellsen on 7/12/19 2:06 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 2:05 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.resetpass

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityResetPassBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ResetPassActivity : BindingActivity<ActivityResetPassBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_reset_pass

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as ResetPassViewModel
    vm.resetPassFormState.observe(this@ResetPassActivity, Observer {
      val resetPassFormState = it ?: return@Observer

      binding.tilUsername.error = if (resetPassFormState.usernameError == null) null
      else getString(resetPassFormState.usernameError)

      // disable login button unless both username / password is valid
      binding.btnResetPass.isEnabled = resetPassFormState.isDataValid
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
