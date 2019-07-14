/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 7:01 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetBranchCodesResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetCardTypesResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetProductTypesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface AdminApi {

  @GET("getProduct")
  fun getProductTypes(): Single<GetProductTypesResponse>

  @GET("getCard")
  fun getCardTypes(): Single<GetCardTypesResponse>

  @GET("getBranch")
  fun getBranchCodes(): Single<GetBranchCodesResponse>

}
