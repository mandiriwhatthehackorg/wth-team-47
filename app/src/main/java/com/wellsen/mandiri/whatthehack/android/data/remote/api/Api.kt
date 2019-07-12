/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/11/19 4:52 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import retrofit2.http.GET

interface Api {

  @GET("MarketingAPI/1.0/promotion")
  fun getPromotions()

}
