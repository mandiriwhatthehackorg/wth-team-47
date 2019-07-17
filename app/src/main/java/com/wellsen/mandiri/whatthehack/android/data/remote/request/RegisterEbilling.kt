/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 10:27 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class RegisterEbilling(

  val creditCardNumber: String,
  val customerName: String,
  val emailAddress: String

)
