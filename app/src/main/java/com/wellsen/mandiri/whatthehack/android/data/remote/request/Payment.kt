/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 10:51 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class Payment(

  val productNumber: String,
  val currency: String,
  val debitAccount: String,
  val paymentAmount: String,
  val billingId: String,
  val companyAccountNumber: String

)
