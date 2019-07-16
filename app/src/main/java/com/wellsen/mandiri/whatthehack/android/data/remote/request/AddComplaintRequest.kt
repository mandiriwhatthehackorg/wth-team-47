/*
 * *
 *  * Created by Wellsen on 7/16/19 1:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 12:50 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

import com.google.gson.annotations.SerializedName

data class AddComplaintRequest(

  @SerializedName("Request")
  val complaint: Complaint

)
