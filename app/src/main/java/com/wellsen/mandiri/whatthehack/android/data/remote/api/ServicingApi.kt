/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 10:31 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.request.RegisterEbillingRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.UnregisterEbillingRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetBalanceResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetCreditCardBalanceResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetCreditCardTransactionsResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetCustomerResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetLoanBalanceResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetLoanTransactionsResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetTransactionsResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.RegisterEbillingResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.UnregisterEbillingResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ServicingApi {

  @GET("customer/{cifNumber}")
  fun getCustomer(@Path("cifNumber") cifNumber: String): Single<GetCustomerResponse>

  @GET("customer/casa/{accountNumber}")
  fun getDailyTransactions(@Path("accountNumber") accountNumber: String): Single<GetTransactionsResponse>

  @GET("customer/casa/{accountNumber}/transaction")
  fun getDailyTransactionsByPeriod(
    @Path("accountNumber") accountNumber: String,
    @Query("startDate") startDate: String,
    @Query("endDate") endDate: String
  ): Single<GetTransactionsResponse>

  @GET("customer/casa/{accountNumber}/balance")
  fun getBalance(@Path("accountNumber") accountNumber: String): Single<GetBalanceResponse>

  @GET("customer/creditcard/{ccNumber}/balance")
  fun getCreditCardBalance(@Path("ccNumber") ccNumber: String): Single<GetCreditCardBalanceResponse>

  @GET("customer/creditcard/{ccNumber}/transaction")
  fun getCreditCardTransactions(
    @Path("ccNumber") ccNumber: String,
    @Query("retrieveRecordNumber") recordNumber: String
  ): Single<GetCreditCardTransactionsResponse>

  @POST("customer/ebilling/{accountNumber}")
  fun registerEbilling(
    @Path("accountNumber") accountNumber: String,
    @Query("action") action: String,
    @Body request: RegisterEbillingRequest
  ): Single<RegisterEbillingResponse>

  @POST("customer/ebilling/{accountNumber}")
  fun unregisterEbilling(
    @Path("accountNumber") accountNumber: String,
    @Query("action") action: String,
    @Body request: UnregisterEbillingRequest
  ): Single<UnregisterEbillingResponse>

  @GET("customer/loan/{accountNumber}/balance")
  fun getLoanBalance(@Path("accountNumber") accountNumber: String): Single<GetLoanBalanceResponse>

  @GET("customer/loan/{accountNumber}/transaction")
  fun getLoanTransactions(
    @Path("accountNumber") accountNumber: String,
    @Query("startDate") startDate: String,
    @Query("endDate") endDate: String
  ): Single<GetLoanTransactionsResponse>

}
