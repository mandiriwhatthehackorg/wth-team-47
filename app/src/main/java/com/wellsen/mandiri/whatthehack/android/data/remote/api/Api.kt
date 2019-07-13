/*
 * *
 *  * Created by Wellsen on 7/13/19 9:53 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/13/19 9:19 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import io.reactivex.Single
import retrofit2.http.GET

interface Api {

  @GET("MarketingAPI/1.0/promotion")
  fun getPromotions(): Single<Void>

}
