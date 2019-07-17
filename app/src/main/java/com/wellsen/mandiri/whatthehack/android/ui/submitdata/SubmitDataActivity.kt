/*
 * *
 *  * Created by Wellsen on 7/17/19 1:45 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 1:02 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitdata

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitDataBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SUBMIT_PHOTO
import com.wellsen.mandiri.whatthehack.android.ui.submitphoto.SubmitPhotoActivity
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

    vm.status.observe(this@SubmitDataActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitDataActivity, it.message, Toast.LENGTH_LONG).show()
        startActivityForResult(Intent(this, SubmitPhotoActivity::class.java), REQUEST_SUBMIT_PHOTO)

      } else {

        // Proceed submit KTP
        Timber.d("Proceed submit KTP")

      }

    })

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    if (requestCode == REQUEST_SUBMIT_PHOTO) {
      setResult(Activity.RESULT_OK)
      finish()
    }
  }

}