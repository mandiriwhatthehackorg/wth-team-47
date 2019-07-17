/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 12:03 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.response.AccountInfoResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.FxRatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SalesApi {

  @GET("fee/mtrAccount")
  fun getMtrAccountInfo(): Single<AccountInfoResponse>

  @GET("fee/savingAccount")
  fun getSavingAccountInfo(): Single<AccountInfoResponse>

  @GET("fee/tabunganValas")
  fun getTabunganValasAccountInfo(): Single<AccountInfoResponse>

  @GET("rates")
  fun getFxRates(): Single<FxRatesResponse>

  @GET("rates/{currency}")
  fun getFxRates(@Path("currency") currency: String): Single<FxRatesResponse>

}
