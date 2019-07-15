/*
 * *
 *  * Created by Wellsen on 7/15/19 1:30 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/15/19 1:30 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitktp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitKtpBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_OPEN_GALLERY
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_TAKE_PHOTO
import com.wellsen.mandiri.whatthehack.android.util.FileUtils.getPath
import id.zelory.compressor.Compressor
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber
import java.io.File

class SubmitKtpActivity : BindingActivity<ActivitySubmitKtpBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_ktp

  lateinit var vm: SubmitKtpViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    vm = binding.vm as SubmitKtpViewModel
    vm.status.observe(this@SubmitKtpActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitKtpActivity, it.message, Toast.LENGTH_LONG).show()
        startActivity(Intent(this, SubmitKtpActivity::class.java))

      } else {

        // Proceed submit Photo
        Timber.d("Proceed submit Photo")

      }

    })

    binding.ivKtp.setOnClickListener {
      selectImage()
    }

  }

  /**
   * Alert dialog for capture or select from galley
   */
  private fun selectImage() {
    val items = arrayOf<CharSequence>(
      getString(R.string.action_take_photo),
      getString(R.string.action_open_gallery)
    )

    AlertDialog.Builder(this@SubmitKtpActivity)
      .setItems(items) { _, item ->
        when (items[item]) {
          getString(R.string.action_take_photo) -> requestCameraPermission()
          getString(R.string.action_open_gallery) -> requestStoragePermission()
        }
      }.show()
  }

  private fun loadPhoto() {
    val layoutParams = binding.ivKtp.layoutParams as LayoutParams
    layoutParams.height = binding.cl.measuredWidth / 16 * 9
    binding.ivKtp.layoutParams = layoutParams
    binding.btnSubmit.isEnabled = true
    vm.photoFile = photoFile

    Glide.with(this)
      .load(photoFile)
      .into(binding.ivKtp)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    when (requestCode) {
      REQUEST_TAKE_PHOTO -> {
        photoFile = Compressor(this)
          .setMaxWidth(1366)
          .setMaxHeight(1366)
          .compressToFile(photoFile)

        loadPhoto()
      }

      REQUEST_OPEN_GALLERY -> {
        val selectedImage = data?.data ?: return
        val path = getPath(this, selectedImage) ?: return

        photoFile = Compressor(this)
          .setMaxWidth(1366)
          .setMaxHeight(1366)
          .compressToFile(File(path))

        loadPhoto()
      }
    }
  }

}
