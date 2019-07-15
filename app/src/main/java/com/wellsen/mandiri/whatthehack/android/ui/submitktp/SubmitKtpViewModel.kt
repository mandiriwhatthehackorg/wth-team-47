/*
 * *
 *  * Created by Wellsen on 7/15/19 1:30 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 12:42 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitktp

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.adapter.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.response.SubmitKtpResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

class SubmitKtpViewModel(
  private val clientApi: ClientApi
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var status = MutableLiveData<Status>()
  lateinit var photoFile: File

  private fun submitKtp() {
    val fileBody = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("file", photoFile.name, fileBody)

    @Suppress("UnstableApiUsage")
    add(
      clientApi.submitKtp(body).with()
        .doOnSubscribe { onSubmitKtpStart() }
        .doOnTerminate { onSubmitKtpFinish() }
        .subscribe(
          { onSubmitKtpSuccess(it) },
          { onSubmitKtpError(it) }
        )
    )
  }

  fun onClickSubmit(@Suppress("unused_parameter") view: View?) {
    submitKtp()
  }

  private fun onSubmitKtpStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onSubmitKtpFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onSubmitKtpSuccess(response: SubmitKtpResponse) {
    Timber.d(response.response)
    status.value = Status(Status.SUCCESS)
  }

  private fun onSubmitKtpError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
  }

}
