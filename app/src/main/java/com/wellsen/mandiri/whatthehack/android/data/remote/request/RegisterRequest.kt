/*
 * *
 *  * Created by Wellsen on 7/20/19 4:08 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 2:56 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class RegisterRequest(

  val email: String,
  var nik: String,
  val phone: String,
  val ttl: String

)
