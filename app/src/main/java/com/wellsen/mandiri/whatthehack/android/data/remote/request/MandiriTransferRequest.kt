/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 11:11 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

import com.google.gson.annotations.SerializedName

data class MandiriTransferRequest(

  @SerializedName("Request")
  val request: MandiriTransfer

)
