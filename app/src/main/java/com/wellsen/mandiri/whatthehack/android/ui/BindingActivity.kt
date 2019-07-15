/*
 * *
 *  * Created by Wellsen on 7/15/19 1:30 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/15/19 1:30 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri.fromParts
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wellsen.mandiri.whatthehack.android.BuildConfig
import com.wellsen.mandiri.whatthehack.android.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

const val REQUEST_APP_SETTINGS = 101
const val REQUEST_TAKE_PHOTO = 102
const val REQUEST_OPEN_GALLERY = 103

abstract class BindingActivity<T : ViewDataBinding> : AppCompatActivity() {

  @LayoutRes
  abstract fun getLayoutResId(): Int

  lateinit var photoFile: File

  protected lateinit var binding: T private set

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, getLayoutResId())
  }

  /**
   * Requesting multiple permissions (storage and camera) at once
   * This uses multiple permission model from dexter
   * On permanent denial opens settings dialog
   */
  fun requestCameraPermission() {
    Dexter.withActivity(this).withPermissions(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.CAMERA
    )
      .withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
          // check if all permissions are granted
          if (report.areAllPermissionsGranted()) {
            dispatchTakePictureIntent()
          }
          // check for permanent denial of any permission
          if (report.isAnyPermissionPermanentlyDenied) {
            // show alert dialog navigating to Settings
            showSettingsDialog(getString(R.string.title_text_permission_needed))
          }
        }

        override fun onPermissionRationaleShouldBeShown(
          permissions: List<PermissionRequest>,
          token: PermissionToken
        ) {
          token.continuePermissionRequest()
        }
      }).withErrorListener {
        Toast.makeText(
          applicationContext,
          "Permission request error",
          Toast.LENGTH_SHORT
        ).show()
      }
      .onSameThread()
      .check()
  }

  /**
   * Requesting multiple permissions (storage) at once
   * This uses multiple permission model from dexter
   * On permanent denial opens settings dialog
   */
  fun requestStoragePermission() {
    Dexter.withActivity(this).withPermissions(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
      .withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
          // check if all permissions are granted
          if (report.areAllPermissionsGranted()) {
            dispatchGalleryIntent()
          }
          // check for permanent denial of any permission
          if (report.isAnyPermissionPermanentlyDenied) {
            // show alert dialog navigating to Settings
            showSettingsDialog(getString(R.string.title_text_permission_needed))
          }
        }

        override fun onPermissionRationaleShouldBeShown(
          permissions: List<PermissionRequest>,
          token: PermissionToken
        ) {
          token.continuePermissionRequest()
        }
      }).withErrorListener {
        Toast.makeText(
          applicationContext,
          "Permission request error",
          Toast.LENGTH_SHORT
        ).show()
      }
      .onSameThread()
      .check()
  }

  /**
   * Showing Alert Dialog with Settings option
   * Navigates user to app settings
   */
  private fun showSettingsDialog(message: String) {
    val builder = AlertDialog.Builder(this)
//    builder.setTitle("Need Permissions")
    builder.setMessage(message)
    builder.setPositiveButton(getString(R.string.action_settings)) { dialog, _ ->
      dialog.cancel()
      openSettings()
    }
    builder.setNegativeButton(getString(R.string.action_cancel)) { dialog, _ -> dialog.cancel() }
    builder.show()
  }

  // navigating user to app settings
  private fun openSettings() {
    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, REQUEST_APP_SETTINGS)
  }

  /**
   * Create file with current timestamp name
   *
   * @return
   * @throws IOException
   */
  @SuppressLint("SimpleDateFormat")
  @Throws(IOException::class)
  private fun createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
    val mFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(mFileName, ".jpg", storageDir)
  }

  /**
   * Capture image from camera
   */
  private fun dispatchTakePictureIntent() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(packageManager) != null) {
      // Create the File where the photo should go
      var photoFile: File? = null
      try {
        photoFile = createImageFile()
      } catch (ex: IOException) {
        ex.printStackTrace()
        // Error occurred while creating the File
      }

      if (photoFile != null) {
        val photoURI = FileProvider.getUriForFile(
          this,
          BuildConfig.APPLICATION_ID + ".provider",
          photoFile
        )

        this.photoFile = photoFile
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)

      }
    }
  }

  /**
   * Select image fro gallery
   */
  private fun dispatchGalleryIntent() {
    startActivityForResult(
      Intent(
        Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      )
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
      REQUEST_OPEN_GALLERY
    )
  }

}
