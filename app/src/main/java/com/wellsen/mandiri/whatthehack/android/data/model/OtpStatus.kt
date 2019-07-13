/*
 * *
 *  * Created by Wellsen on 7/13/19 8:53 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/13/19 8:50 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.model

data class OtpStatus(

  val code: Int,
  val message: String = ""

) {

  companion object {

    const val OTP_VALIDATION_SUCCESS = 21
    const val OTP_RESEND_SUCCESS = 22
    const val ERROR = 4

  }

}
