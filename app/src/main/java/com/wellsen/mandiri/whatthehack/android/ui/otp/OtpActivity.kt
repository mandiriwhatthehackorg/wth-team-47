/*
 * *
 *  * Created by Wellsen on 7/13/19 8:53 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/13/19 8:51 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.otp

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.OtpStatus
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityOtpBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class OtpActivity : BindingActivity<ActivityOtpBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_otp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as OtpViewModel

    vm.status.observe(this@OtpActivity, Observer {

      when (it.code) {
        OtpStatus.ERROR ->
          Toast.makeText(this@OtpActivity, it.message, Toast.LENGTH_LONG).show()

        OtpStatus.OTP_RESEND_SUCCESS -> {
          binding.viewOtp.text?.clear()
          Toast.makeText(this@OtpActivity, R.string.message_otp_resend, Toast.LENGTH_LONG).show()
        }

        OtpStatus.OTP_VALIDATION_SUCCESS ->
          // Proceed submit data
          Timber.d("Proceed submit data")
      }

    })

    binding.viewOtp.setOtpCompletionListener {
      vm.onOtpFilled(it)
    }
  }

}
