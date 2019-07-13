/*
 * *
 *  * Created by Wellsen on 7/13/19 8:53 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/13/19 8:50 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.otp

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.data.model.OtpStatus
import com.wellsen.mandiri.whatthehack.android.data.remote.api.OnboardingApi
import com.wellsen.mandiri.whatthehack.android.data.remote.request.OtpResendRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.OtpValidationRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.OtpResendResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.OtpValidationResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import timber.log.Timber

class OtpViewModel(
  private val onboardingApi: OnboardingApi
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> = NonNullMutableLiveData(View.INVISIBLE)
  var otpTitle: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var otp: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var status = MutableLiveData<OtpStatus>()

  init {

  }

  fun onOtpFilled(otp: String) {
    @Suppress("UnstableApiUsage")
    add(
      onboardingApi.validateOtp(OtpValidationRequest(otp)).with()
        .doOnSubscribe { onOtpRequestStart() }
        .doOnTerminate { onOtpResponseFinish() }
        .subscribe(
          { onOtpValidationSuccess(it) },
          { onOtpResponseError(it) }
        )
    )
  }

  fun onClickResend(@Suppress("unused_parameter") view: View?) {
    @Suppress("UnstableApiUsage")
    add(
      onboardingApi.resendOtp(OtpResendRequest("1234567890123456")).with()
        .doOnSubscribe { onOtpRequestStart() }
        .doOnTerminate { onOtpResponseFinish() }
        .subscribe(
          { onOtpResendSuccess(it) },
          { onOtpResponseError(it) }
        )
    )
  }

  private fun onOtpRequestStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onOtpResponseFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onOtpValidationSuccess(response: OtpValidationResponse) {
    Timber.d(response.response)
    status.value = OtpStatus(OtpStatus.OTP_VALIDATION_SUCCESS)
  }

  private fun onOtpResendSuccess(response: OtpResendResponse) {
    Timber.d(response.response)
    status.value = OtpStatus(OtpStatus.OTP_RESEND_SUCCESS)
  }

  private fun onOtpResponseError(t: Throwable) {
    Timber.e(t)
    status.value = OtpStatus(OtpStatus.ERROR, t.localizedMessage)
  }

}
