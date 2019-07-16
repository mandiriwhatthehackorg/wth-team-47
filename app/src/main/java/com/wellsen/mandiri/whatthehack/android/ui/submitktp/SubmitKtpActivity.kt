/*
 * *
 *  * Created by Wellsen on 7/16/19 5:22 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 5:22 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitktp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitKtpBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.KTP
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_KTP_OPEN_GALLERY
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_KTP_TAKE_PHOTO
import com.wellsen.mandiri.whatthehack.android.ui.register.RegisterActivity
import com.wellsen.mandiri.whatthehack.android.util.DOB
import com.wellsen.mandiri.whatthehack.android.util.FileUtils
import com.wellsen.mandiri.whatthehack.android.util.KTP_FILE
import com.wellsen.mandiri.whatthehack.android.util.NAME
import com.wellsen.mandiri.whatthehack.android.util.NIK
import id.zelory.compressor.Compressor
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.File
import java.io.IOException

class SubmitKtpActivity : BindingActivity<ActivitySubmitKtpBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_ktp

  val sp: SharedPreferences by inject()

  lateinit var path: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.lifecycleOwner = this

    binding.ivKtp.setOnClickListener {
      selectKtp()
    }

    binding.btnSubmit.setOnClickListener {
      submit()
    }
  }

  private fun submit() {
    startActivity(Intent(this, RegisterActivity::class.java))
  }

  /**
   * Alert dialog for capture or select from galley
   */
  private fun selectKtp() {
    val items = arrayOf<CharSequence>(
      getString(R.string.action_take_photo),
      getString(R.string.action_open_gallery)
    )

    AlertDialog.Builder(this@SubmitKtpActivity)
      .setItems(items) { _, item ->
        when (items[item]) {
          getString(R.string.action_take_photo) -> requestCameraPermission(KTP)
          getString(R.string.action_open_gallery) -> requestStoragePermission(KTP)
        }
      }.show()
  }

  private fun loadPhoto(type: Int) {
    sp.edit().putString(KTP_FILE, Uri.fromFile(ktpFile).toString()).apply()

    when (type) {
      KTP -> {
        val lp = binding.ivKtp.layoutParams as LayoutParams
        lp.height = binding.cl.measuredWidth / 16 * 9
        binding.ivKtp.layoutParams = lp
        binding.btnSubmit.isEnabled = true

        Glide.with(this)
          .load(ktpFile)
          .into(binding.ivKtp)
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    when (requestCode) {
      REQUEST_KTP_TAKE_PHOTO -> processOcr(Uri.fromFile(ktpFile), REQUEST_KTP_TAKE_PHOTO)

      REQUEST_KTP_OPEN_GALLERY -> {
        val selectedImage = data?.data ?: return
        path = FileUtils.getPath(this, selectedImage) ?: return

        processOcr(selectedImage, REQUEST_KTP_OPEN_GALLERY)
      }
    }
  }

  private fun processOcr(uri: Uri, code: Int) {
    val image: FirebaseVisionImage
    try {
      image = FirebaseVisionImage.fromFilePath(this, uri)
    } catch (e: IOException) {
      e.printStackTrace()
      return
    }

    binding.pb.visibility = View.VISIBLE

    FirebaseVision.getInstance().getCloudTextRecognizer(
      FirebaseVisionCloudTextRecognizerOptions.Builder()
        .setLanguageHints(mutableListOf("id"))
        .build()
    ).processImage(image)
      .addOnSuccessListener {
        processResult(it)

        when (code) {
          REQUEST_KTP_TAKE_PHOTO -> {
            ktpFile = Compressor(this)
              .setMaxWidth(1366)
              .setMaxHeight(1366)
              .compressToFile(ktpFile)
          }
          REQUEST_KTP_OPEN_GALLERY -> {
            ktpFile = Compressor(this)
              .setMaxWidth(1366)
              .setMaxHeight(1366)
              .compressToFile(File(path))
          }
        }

        loadPhoto(KTP)
        binding.pb.visibility = View.GONE
      }
      .addOnFailureListener {
        Timber.e(it.localizedMessage)
        binding.pb.visibility = View.GONE
      }
  }

  fun processResult(text: FirebaseVisionText) {
    for (block in text.textBlocks) {
      val blockText = block.text

      if (blockText.trim().length == 16) {
        if (TextUtils.isDigitsOnly(blockText.trim())) {
          sp.edit().putString(NIK, blockText.trim()).apply()
        }
        continue
      }

      if (blockText.trim().contains(",")) {
        if (blockText.contains('\n')) {
          val name = blockText.substring(0, blockText.indexOf('\n'))
          sp.edit().putString(NAME, name).apply()

          val dob = blockText.trim().substring(blockText.trim().length - 10)
          sp.edit().putString(DOB, dob).apply()
        }

      }

    }

  }

}
