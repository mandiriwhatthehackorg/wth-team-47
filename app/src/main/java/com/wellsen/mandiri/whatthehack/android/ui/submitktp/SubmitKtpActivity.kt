/*
 * *
 *  * Created by Wellsen on 7/20/19 4:08 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 4:07 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitktp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitKtpBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.EXTRA_CHILD
import com.wellsen.mandiri.whatthehack.android.ui.JPG
import com.wellsen.mandiri.whatthehack.android.ui.KTP
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_BARCODE
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_KTP_OPEN_GALLERY
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_KTP_TAKE_PHOTO
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_REGISTER
import com.wellsen.mandiri.whatthehack.android.ui.register.RegisterActivity
import com.wellsen.mandiri.whatthehack.android.ui.scanner.QrCodeScannerActivity
import com.wellsen.mandiri.whatthehack.android.util.DATE_FORMAT
import com.wellsen.mandiri.whatthehack.android.util.DOB
import com.wellsen.mandiri.whatthehack.android.util.FileUtils
import com.wellsen.mandiri.whatthehack.android.util.KTP_FILE
import com.wellsen.mandiri.whatthehack.android.util.NAME
import com.wellsen.mandiri.whatthehack.android.util.NIK
import id.zelory.compressor.Compressor
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat

class SubmitKtpActivity : BindingActivity<ActivitySubmitKtpBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_ktp

  private val sp: SharedPreferences by inject()

  lateinit var path: String
  var child: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.lifecycleOwner = this

    binding.ivKtp.setOnClickListener {
      selectKtp()
    }

    binding.btnSubmit.setOnClickListener {
      submit()
    }

    binding.btnChild.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setMessage("Scan QR code di aplikasi Mandiri Online orang tua kamu")

      builder.setPositiveButton(android.R.string.yes) { dialog, _ ->
        dialog.dismiss()
        startActivityForResult(Intent(this, QrCodeScannerActivity::class.java), REQUEST_BARCODE)
      }

      builder.show()
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    sp.edit().remove(KTP_FILE).apply()
    sp.edit().remove(NAME).apply()
    sp.edit().remove(NIK).apply()
    sp.edit().remove(DOB).apply()
  }

  private fun submit() {
    val intent = Intent(this, RegisterActivity::class.java)
    intent.putExtra(EXTRA_CHILD, child)
    startActivityForResult(intent, REQUEST_REGISTER)
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

    child = false

    when (type) {
      KTP -> {
//        val lp = binding.ivKtp.layoutParams as LayoutParams
//        lp.height = binding.cl.measuredWidth/* / 16 * 9*/
//        binding.ivKtp.layoutParams = lp
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
      REQUEST_BARCODE -> {
        ktpFile = createImageFile(JPG)
        val bm = BitmapFactory.decodeResource(resources, R.drawable.frame)

        saveBitmapToFile(bm, Uri.fromFile(ktpFile))

        sp.edit().putString(KTP_FILE, Uri.fromFile(ktpFile).toString()).apply()

        Glide.with(this)
          .load(R.drawable.frame)
          .into(binding.ivKtp)

        child = true

        binding.btnSubmit.isEnabled = true
      }

      REQUEST_KTP_TAKE_PHOTO -> {
        normalizeImage(Uri.fromFile(ktpFile))

        ktpFile = Compressor(this)
          .setMaxWidth(1366)
          .setMaxHeight(1366)
          .compressToFile(ktpFile)

        processOcr(Uri.fromFile(ktpFile), REQUEST_KTP_TAKE_PHOTO)
      }

      REQUEST_KTP_OPEN_GALLERY -> {
        val selectedImage = data?.data ?: return
        path = FileUtils.getPath(this, selectedImage) ?: return

        ktpFile = Compressor(this)
          .setMaxWidth(1366)
          .setMaxHeight(1366)
          .compressToFile(File(path))

        processOcr(Uri.fromFile(ktpFile), REQUEST_KTP_OPEN_GALLERY)
      }

      REQUEST_REGISTER -> {
        setResult(Activity.RESULT_OK)
        finish()
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
        loadPhoto(KTP)
        binding.pb.visibility = View.GONE
      }
  }

  private fun processResult(text: FirebaseVisionText) {
    sp.edit().remove(DOB).apply()
    sp.edit().remove(NIK).apply()
    var namePos: Int = -1
    val words = text.text.split("\n")
    for (i in words.indices) {
      if (words[i].replace(" ", "").length == 16) {
        if (TextUtils.isDigitsOnly(words[i])) {
          sp.edit().putString(NIK, words[i]).apply()
        }
        continue
      }
      if (words[i].replace(" ", "").toLowerCase() == "nik") {
        namePos = i + 1
        continue
      }
      if (i == namePos) {
        if (sp.getString(NAME, null) == null) {
          sp.edit().putString(NAME, words[i]).apply()
        }
        continue
      }
      if (words[i].length >= 10) {
        if (words[i].substring(words[i].length - 4, words[i].length - 2) == "19") {
          var dob = words[i].substring(words[i].length - 10)
          dob = dob.replace(" ", "-")
          if (isDateValid(dob)) {
            sp.edit().putString(DOB, dob).apply()
          }
        }
      }

    }
    for (block in text.textBlocks) {
      val blockText = block.text

      if (blockText.trim().length == 16) {
        if (TextUtils.isDigitsOnly(blockText.trim())) {
          if (sp.getString(NIK, null) == null) {
            sp.edit().putString(NIK, blockText.trim()).apply()
          }
        }
        continue
      }

      if (blockText.trim().contains(",")) {
        if (blockText.contains('\n')) {
          val name = blockText.substring(0, blockText.indexOf('\n'))
          if (sp.getString(NAME, null) == null) {
            sp.edit().putString(NAME, name).apply()
          }

          val dob = blockText.trim().substring(blockText.trim().length - 10)
          if (isDateValid(dob)) {
            if (sp.getString(DOB, null) == null) {
              sp.edit().putString(DOB, dob).apply()
            }
          }
        }

      }

    }

  }

  @SuppressLint("SimpleDateFormat")
  fun isDateValid(date: String): Boolean {
    return try {
      val df = SimpleDateFormat(DATE_FORMAT)
      df.isLenient = false
      df.parse(date)
      true
    } catch (e: Exception) {
      false
    }

  }

  private fun normalizeImage(uri: Uri) {
    if (uri.path == null) {
      return
    }
    try {
      val exif = ExifInterface(uri.path!!)
      val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
      val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
      val rotatedBitmap = rotateBitmap(bitmap, orientation)
      if (bitmap != rotatedBitmap) {
        saveBitmapToFile(rotatedBitmap, uri)
      }
    } catch (e: IOException) {
      Timber.e(e.localizedMessage)
    }
  }

  private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
    val matrix = Matrix()
    when (orientation) {
      ExifInterface.ORIENTATION_NORMAL -> return bitmap

      ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1.0f, 1.0f)

      ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180.0f)

      ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
        matrix.setRotate(180.0f)
        matrix.postScale(-1.0f, 1.0f)
      }

      ExifInterface.ORIENTATION_TRANSPOSE -> {
        matrix.setRotate(90.0f)
        matrix.postScale(-1.0f, 1.0f)
      }

      ExifInterface.ORIENTATION_ROTATE_90 -> {
        matrix.setRotate(90.0f)
      }

      ExifInterface.ORIENTATION_TRANSVERSE -> {
        matrix.setRotate(-90.0f)
        matrix.postScale(-1.0f, 1.0f)
      }

      ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90.0f)

      else -> return bitmap
    }

    return try {
      val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
      bitmap.recycle()

      bmRotated
    } catch (e: OutOfMemoryError) {
      Timber.e(e.localizedMessage)
      bitmap
    }

  }

  private fun saveBitmapToFile(croppedImage: Bitmap, saveUri: Uri) {
    var outputStream: OutputStream? = null
    try {
      outputStream = this.contentResolver.openOutputStream(saveUri)
      croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    } catch (e: IOException) {
      Timber.e(e.localizedMessage)
    } finally {
      outputStream?.let { closeSilently(it) }
      croppedImage.recycle()
    }
  }

  private fun closeSilently(c: Closeable) {
    try {
      c.close()
    } catch (t: Throwable) {
      Timber.e(t.localizedMessage)
    }
  }

}
