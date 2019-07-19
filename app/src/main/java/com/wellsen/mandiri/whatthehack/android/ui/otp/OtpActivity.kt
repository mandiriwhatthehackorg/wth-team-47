/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 8:44 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.otp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.OtpStatus
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityOtpBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.EXTRA_STATUS
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SUBMIT_DATA
import com.wellsen.mandiri.whatthehack.android.ui.submitdata.SubmitDataActivity
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

    if (intent.getBooleanExtra(EXTRA_STATUS, false)) {
      vm.onClickResend(null)
    }

    vm.status.observe(this@OtpActivity, Observer {

      when (it.code) {
        OtpStatus.ERROR -> {
          Toast.makeText(this@OtpActivity, it.message, Toast.LENGTH_LONG).show()
        }

        OtpStatus.OTP_RESEND_SUCCESS -> {
          binding.viewOtp.text?.clear()
          Toast.makeText(this@OtpActivity, R.string.message_otp_resend, Toast.LENGTH_LONG).show()
        }

        OtpStatus.OTP_VALIDATION_SUCCESS -> {
          // Proceed submit data
          Timber.d("Proceed submit data")
          startActivityForResult(Intent(this, SubmitDataActivity::class.java), REQUEST_SUBMIT_DATA)
        }

      }

    })

    binding.viewOtp.setOtpCompletionListener {
      vm.onOtpFilled(it)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    if (requestCode == REQUEST_SUBMIT_DATA) {
      setResult(Activity.RESULT_OK)
      finish()
    }
  }

}
