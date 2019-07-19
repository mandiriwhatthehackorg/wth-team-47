/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 10:50 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.response

data class GetCardTypesResponse(

  val success: Boolean,
  val data: GetCardType,
  val message: String

)
