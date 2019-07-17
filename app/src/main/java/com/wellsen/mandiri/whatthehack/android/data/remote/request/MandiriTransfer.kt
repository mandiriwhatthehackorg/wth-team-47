/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 11:11 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class MandiriTransfer(

  val transactionID: String,
  val transactionDate: String,
  val referenceID: String,
  val sourceAccountNumber: String,
  val beneficiaryAccountNumber: String,
  val amount: String,
  val currency: String,
  val sourceAccountCustType: String,
  val beneficiaryCustType: String,
  val remark1: String,
  val remark2: String

)