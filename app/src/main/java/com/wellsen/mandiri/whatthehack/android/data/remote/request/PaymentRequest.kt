/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 10:40 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

import com.google.gson.annotations.SerializedName

data class PaymentRequest(

  @SerializedName("Request")
  val request: Payment

)
