/*
 * *
 *  * Created by Wellsen on 7/16/19 1:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 1:03 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

import com.google.gson.annotations.SerializedName

data class AddFeedbackRequest(

  @SerializedName("Request")
  val addFeedback: AddFeedback

)
