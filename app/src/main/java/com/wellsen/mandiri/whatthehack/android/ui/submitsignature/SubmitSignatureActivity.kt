/*
 * *
 *  * Created by Wellsen on 7/16/19 10:58 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 10:57 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitsignature

import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitSignatureBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.PNG
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class SubmitSignatureActivity : BindingActivity<ActivitySubmitSignatureBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_signature

  lateinit var vm: SubmitSignatureViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

    super.onCreate(savedInstanceState)

    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    vm = binding.vm as SubmitSignatureViewModel
    vm.pad = binding.sp
    vm.signatureFile = createImageFile(PNG)

    vm.status.observe(this@SubmitSignatureActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitSignatureActivity, it.message, Toast.LENGTH_LONG).show()

      } else {

        // Proceed main page
        Timber.d("Proceed main page")

      }

    })

    binding.btnCancel.setOnClickListener {
      finish()
    }

  }

}
