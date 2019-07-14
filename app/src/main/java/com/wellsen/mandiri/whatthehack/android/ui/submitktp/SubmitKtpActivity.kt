/*
 * *
 *  * Created by Wellsen on 7/14/19 9:31 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 9:30 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitktp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitKtpBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class SubmitKtpActivity : BindingActivity<ActivitySubmitKtpBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_ktp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as SubmitKtpViewModel
    vm.status.observe(this@SubmitKtpActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitKtpActivity, it.message, Toast.LENGTH_LONG).show()
        startActivity(Intent(this, SubmitKtpActivity::class.java))

      } else {

        // Proceed submit Photo
        Timber.d("Proceed submit Photo")

      }

    })

    binding.btnTakePhoto.setOnClickListener {

    }

    binding.btnOpenGallery.setOnClickListener {

    }

  }

}
