/*
 * *
 *  * Created by Wellsen on 7/16/19 1:05 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 1:05 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.request.AddComplaintRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.AddFeedbackRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.AddComplaintResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.AddFeedbackResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetComplaintResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetFeedbackResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerExperienceApi {

  @POST("complaint")
  fun addComplaint(@Body request: AddComplaintRequest): Single<AddComplaintResponse>

  @GET("complaint/{ticketId}")
  fun getComplaint(@Path("ticketId") ticketId: String): Single<GetComplaintResponse>

  @PUT("feedback")
  fun addFeedback(@Body request: AddFeedbackRequest): Single<AddFeedbackResponse>

  @GET("feedback/{ticketId}")
  fun getFeedback(@Path("ticketId") ticketId: String): Single<GetFeedbackResponse>

}
