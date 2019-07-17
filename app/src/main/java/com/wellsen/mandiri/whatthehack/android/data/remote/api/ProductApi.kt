/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 10:10 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetProductsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ProductApi {

  @GET("product")
  fun getProducts(): Single<GetProductsResponse>

  @GET("product/creditCard")
  fun getCreditCardProducts(): Single<GetProductsResponse>

  @GET("product/debitCard")
  fun getDebitCardProducts(): Single<GetProductsResponse>

  @GET("product/personalLoan")
  fun getPersonalLoanProducts(): Single<GetProductsResponse>

  @GET("product/savingAccount")
  fun getSavingAccountProducts(): Single<GetProductsResponse>

  @GET("product/timeDeposit")
  fun getTimeDepositProducts(): Single<GetProductsResponse>

}
