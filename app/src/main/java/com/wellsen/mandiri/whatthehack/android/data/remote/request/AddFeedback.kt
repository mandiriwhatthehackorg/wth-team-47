/*
 * *
 *  * Created by Wellsen on 7/16/19 1:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 1:03 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.request

data class AddFeedback(

  val email: String,
  val eventId: String,
  val subject: String,
  val origin: String,
  val feedback: List<Feedback>,
  val closingStatement: String,
  val closingRating: String

)