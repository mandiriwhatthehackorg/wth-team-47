/*
 * *
 *  * Created by Wellsen on 7/15/19 4:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/15/19 3:54 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitphoto

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.adapter.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.response.SubmitKtpResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.SubmitSelfieResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

class SubmitPhotoViewModel(
  private val clientApi: ClientApi
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var status = MutableLiveData<Status>()
  lateinit var ktpFile: File
  lateinit var selfieFile: File

  private fun submitKtp() {
    val fileBody = ktpFile.asRequestBody("image/*".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("file", ktpFile.name, fileBody)

    @Suppress("UnstableApiUsage")
    add(
      clientApi.submitKtp(body).with()
        .doOnSubscribe { onSubmitPhotoStart() }
        .doOnTerminate { onSubmitPhotoFinish() }
        .subscribe(
          { onSubmitKtpSuccess(it) },
          { onSubmitPhotoError(it) }
        )
    )
  }

  private fun submitSelfie() {
    val fileBody = selfieFile.asRequestBody("image/*".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("file", selfieFile.name, fileBody)

    @Suppress("UnstableApiUsage")
    add(
      clientApi.submitSelfie(body).with()
        .doOnSubscribe { onSubmitPhotoStart() }
        .doOnTerminate { onSubmitPhotoFinish() }
        .subscribe(
          { onSubmitSelfieSuccess(it) },
          { onSubmitPhotoError(it) }
        )
    )
  }

  fun onClickSubmit(@Suppress("unused_parameter") view: View?) {
    submitKtp()
  }

  private fun onSubmitPhotoStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onSubmitPhotoFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onSubmitKtpSuccess(response: SubmitKtpResponse) {
    Timber.d(response.response)
    submitSelfie()
  }

  private fun onSubmitSelfieSuccess(response: SubmitSelfieResponse) {
    Timber.d(response.response)
    status.value = Status(Status.SUCCESS)
  }

  private fun onSubmitPhotoError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
  }

}
