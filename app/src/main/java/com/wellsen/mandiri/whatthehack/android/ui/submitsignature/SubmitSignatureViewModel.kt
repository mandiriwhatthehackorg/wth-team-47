/*
 * *
 *  * Created by Wellsen on 7/16/19 10:58 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 10:57 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitsignature

import android.graphics.Bitmap.CompressFormat
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.github.gcacace.signaturepad.views.SignaturePad
import com.wellsen.mandiri.whatthehack.android.adapter.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.response.SubmitSignatureResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class SubmitSignatureViewModel(
  private val clientApi: ClientApi
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var btnAgreeEnabled: NonNullMutableLiveData<Boolean> =
    NonNullMutableLiveData(false)
  var status = MutableLiveData<Status>()
  lateinit var signatureFile: File
  lateinit var pad: SignaturePad

  private fun sign() {
    //Convert bitmap to byte array
    val bitmap = pad.getTransparentSignatureBitmap(true)
    val bos = ByteArrayOutputStream()
    bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitmapData = bos.toByteArray()

    //write the bytes in file
    val fos = FileOutputStream(signatureFile)
    fos.write(bitmapData)
    fos.flush()
    fos.close()

    val fileBody = signatureFile.asRequestBody("image/*".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("file", signatureFile.name, fileBody)

    @Suppress("UnstableApiUsage")
    add(
      clientApi.submitSignature(body).with()
        .doOnSubscribe { onSubmitSignatureStart() }
        .doOnTerminate { onSubmitSignatureFinish() }
        .subscribe(
          { onSubmitSignatureSuccess(it) },
          { onSubmitSignatureError(it) }
        )
    )
  }

  private fun onSubmitSignatureStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onSubmitSignatureFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onSubmitSignatureSuccess(response: SubmitSignatureResponse) {
    Timber.d(response.response)
    status.value = Status(Status.SUCCESS)
  }

  private fun onSubmitSignatureError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
  }

  fun onStartSigning() {

  }

  fun onSigned() {
    btnAgreeEnabled.value = true
  }

  fun onClear() {
    btnAgreeEnabled.value = false
  }

  fun onClickSign(@Suppress("unused_parameter") view: View) {
    sign()
  }

}
