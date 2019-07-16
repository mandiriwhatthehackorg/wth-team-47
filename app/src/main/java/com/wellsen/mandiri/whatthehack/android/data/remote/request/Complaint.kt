/*
 * *
 *  * Created by Wellsen on 7/16/19 1:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 12:50 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class Complaint(

  val contactId: String,
  val type: String,
  val origin: String,
  val subject: String,
  val reason: String,
  val priority: String,
  val status: String,
  val ownerId: String,
  val parentId: String,
  val accountId: String,
  val description: String,
  val isEscalated: String,
  val suppliedName: String,
  val suppliedEmail: String,
  val suppliedPhone: String,
  val suppliedCompany: String,
  val ccEmails: List<String>

)
