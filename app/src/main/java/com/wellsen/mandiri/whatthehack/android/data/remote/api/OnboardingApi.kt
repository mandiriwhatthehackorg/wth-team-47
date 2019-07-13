/*
 * *
 *  * Created by Wellsen on 7/13/19 8:53 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/13/19 8:35 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.request.LoginRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.OtpResendRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.OtpValidationRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.RegisterRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.LoginResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.OtpResendResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.OtpValidationResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.RegisterResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface OnboardingApi {

  @POST("login")
  fun login(@Body request: LoginRequest): Single<LoginResponse>

  @POST("initiate/createSession")
  fun register(@Body request: RegisterRequest): Single<RegisterResponse>

  @POST("initiate/validateOTP")
  fun validateOtp(@Body request: OtpValidationRequest): Single<OtpValidationResponse>

  @POST("initiate/resendOTP")
  fun resendOtp(@Body request: OtpResendRequest): Single<OtpResendResponse>

}
