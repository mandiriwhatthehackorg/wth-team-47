/*
 * *
 *  * Created by Wellsen on 7/14/19 9:31 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 9:17 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitdata

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitDataBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.submitktp.SubmitKtpActivity
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class SubmitDataActivity : BindingActivity<ActivitySubmitDataBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_data

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as SubmitDataViewModel
    vm.submitDataFormState.observe(this@SubmitDataActivity, Observer {
      val submitDataFormState = it ?: return@Observer

      binding.tilMothersMaidenName.error = if (submitDataFormState.mothersNameError == null) null
      else getString(submitDataFormState.mothersNameError)

      // disable login button unless both username / password is valid
      binding.btnSubmit.isEnabled = submitDataFormState.isDataValid
    })

    vm.status.observe(this@SubmitDataActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitDataActivity, it.message, Toast.LENGTH_LONG).show()
        startActivity(Intent(this, SubmitKtpActivity::class.java))

      } else {

        // Proceed submit KTP
        Timber.d("Proceed submit KTP")

      }

    })

    binding.etMothersName.afterTextChanged {
      vm.onRegisterFormChanged(
        binding.etMothersName.text.toString()
      )
    }

  }

}