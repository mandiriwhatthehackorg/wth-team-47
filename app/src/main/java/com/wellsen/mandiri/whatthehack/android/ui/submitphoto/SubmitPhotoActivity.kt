/*
 * *
 *  * Created by Wellsen on 7/19/19 11:14 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 10:51 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitphoto

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
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitPhotoBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SELFIE_OPEN_GALLERY
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SELFIE_TAKE_PHOTO
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SUBMIT_SIGNATURE
import com.wellsen.mandiri.whatthehack.android.ui.SELFIE
import com.wellsen.mandiri.whatthehack.android.ui.submitsignature.SubmitSignatureActivity
import com.wellsen.mandiri.whatthehack.android.util.FileUtils.getPath
import id.zelory.compressor.Compressor
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber
import java.io.File

class SubmitPhotoActivity : BindingActivity<ActivitySubmitPhotoBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_photo

  lateinit var vm: SubmitPhotoViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    vm = binding.vm as SubmitPhotoViewModel
    vm.status.observe(this@SubmitPhotoActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitPhotoActivity, it.message, Toast.LENGTH_LONG).show()

      } else {

        // Proceed submit Photo
        Timber.d("Proceed submit Photo")
        startActivityForResult(
          Intent(this, SubmitSignatureActivity::class.java),
          REQUEST_SUBMIT_SIGNATURE
        )

      }

    })

    binding.ivSelfie.setOnClickListener {
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

    AlertDialog.Builder(this@SubmitPhotoActivity)
      .setItems(items) { _, item ->
        when (items[item]) {
          getString(R.string.action_take_photo) -> requestCameraPermission(SELFIE)
          getString(R.string.action_open_gallery) -> requestStoragePermission(SELFIE)
        }
      }.show()
  }

  private fun loadPhoto(type: Int) {
    when (type) {
      SELFIE -> {
        val layoutParams = binding.ivSelfie.layoutParams as LayoutParams
        layoutParams.height = binding.cl.measuredWidth/* / 9 * 16*/
        binding.ivSelfie.layoutParams = layoutParams
        binding.btnSubmit.isEnabled = true
        vm.selfieFile = selfieFile

        Glide.with(this)
          .load(selfieFile)
          .into(binding.ivSelfie)
      }
    }

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    when (requestCode) {
      REQUEST_SELFIE_TAKE_PHOTO -> {
        selfieFile = Compressor(this)
          .setMaxWidth(1366)
          .setMaxHeight(1366)
          .compressToFile(selfieFile)

        loadPhoto(SELFIE)
      }

      REQUEST_SELFIE_OPEN_GALLERY -> {
        val selectedImage = data?.data ?: return
        val path = getPath(this, selectedImage) ?: return

        selfieFile = Compressor(this)
          .setMaxWidth(1366)
          .setMaxHeight(1366)
          .compressToFile(File(path))

        loadPhoto(SELFIE)
      }

      REQUEST_SUBMIT_SIGNATURE -> {
        setResult(Activity.RESULT_OK)
        finish()
      }
    }
  }

}
