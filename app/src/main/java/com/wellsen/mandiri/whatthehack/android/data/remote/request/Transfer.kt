/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 11:12 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class Transfer(

  val transactionID: String,
  val transactionDate: String,
  val referenceID: String,
  val creditAccount: String,
  val sourceAccountNumber: String,
  val sourceBankCode: String,
  val sourceName: String,
  val beneficiaryAccountNumber: String,
  val beneficiaryBankCode: String,
  val beneficiaryName: String,
  val amount: String,
  val currency: String,
  val remark1: String,
  val remark2: String

)
