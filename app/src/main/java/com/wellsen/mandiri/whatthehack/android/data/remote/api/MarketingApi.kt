/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 12:04 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetPromotionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarketingApi {

  @GET("promotion")
  fun getPromotions(): Single<GetPromotionResponse>

  @GET("promotion/period")
  fun getPromotionsByPeriod(
    @Query("startDate") startDate: String,
    @Query("endDate") endDate: String
  ): Single<GetPromotionResponse>

  @GET("promotion/value")
  fun getPromotionsByValue(): Single<GetPromotionResponse>

  @GET("promotion/merchant")
  fun getMerchantPromotions(): Single<GetPromotionResponse>

  @GET("promotion/merchant/{merchantId}")
  fun getMerchantPromotions(@Path("merchantId") merchantId: String): Single<GetPromotionResponse>

}
