/*
 * *
 *  * Created by Wellsen on 7/14/19 9:31 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 9:30 AM
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
import timber.log.Timber

class SubmitKtpViewModel(
  private val clientApi: ClientApi
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var status = MutableLiveData<Status>()

  fun submitKtp() {
//    @Suppress("UnstableApiUsage")
//    add(
//      clientApi.submitKtp().with()
//        .doOnSubscribe { onSubmitKtpStart() }
//        .doOnTerminate { onSubmitKtpFinish() }
//        .subscribe(
//          { onSubmitKtpSuccess(it) },
//          { onSubmitKtpError(it) }
//        )
//    )
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
