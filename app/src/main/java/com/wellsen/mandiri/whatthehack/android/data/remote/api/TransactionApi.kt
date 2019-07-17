/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 12:04 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.api

import com.wellsen.mandiri.whatthehack.android.data.remote.request.CreditCardPaymentRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.MandiriTransferRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.PaymentRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.request.TransferRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetBillsResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetCellularProductsResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.PaymentResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionApi {

  @GET("bill")
  fun getBills(): Single<GetBillsResponse>

  @GET("bill/axafinancial/{customerId}")
  fun axaFinancialInquiry(@Path("customerId") customerId: String): Single<PaymentResponse>

  @POST("bill/axafinancial/{customerId}")
  fun axaFinancialPayment(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/education/{customerId}")
  fun educationInquiry(@Path("customerId") customerId: String): Single<PaymentResponse>

  @POST("bill/education/{customerId}")
  fun educationPayment(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/firstmedia/{customerId}")
  fun firstMediaInquiry(@Path("customerId") customerId: String): Single<PaymentResponse>

  @POST("bill/firstmedia/{customerId}")
  fun firstMediaPayment(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/indosat")
  fun getIndosatProducts(): Single<GetCellularProductsResponse>

  @GET("bill/indosat/{product}/{msisdn}")
  fun indosatInquiry(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String
  ): Single<PaymentResponse>

  @POST("bill/indosat/{product}/{msisdn}")
  fun indosatPayment(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/kai/{bookingCode}")
  fun kaiInquiry(
    @Path("bookingCode") bookingCode: String
  ): Single<PaymentResponse>

  @POST("bill/kai/{bookingCode}")
  fun kaiPayment(
    @Path("bookingCode") bookingCode: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/pam/{customerId}")
  fun pamInquiry(
    @Path("customerId") customerId: String
  ): Single<PaymentResponse>

  @POST("bill/pam/{customerId}")
  fun pamPayment(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/pln/{customerId}")
  fun plnInquiry(
    @Path("customerId") customerId: String
  ): Single<PaymentResponse>

  @POST("bill/pln/{customerId}")
  fun plnPayment(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/telkomsel")
  fun getTelkomselProducts(): Single<GetCellularProductsResponse>

  @GET("bill/telkomsel/{product}/{msisdn}")
  fun telkomselInquiry(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String
  ): Single<PaymentResponse>

  @POST("bill/telkomsel/{product}/{msisdn}")
  fun telkomselPayment(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/three")
  fun getThreeProducts(): Single<GetCellularProductsResponse>

  @GET("bill/three/{product}/{msisdn}")
  fun threeInquiry(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String
  ): Single<PaymentResponse>

  @POST("bill/three/{product}/{msisdn}")
  fun threePayment(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/tv/{customerId}")
  fun tvInquiry(
    @Path("customerId") customerId: String
  ): Single<PaymentResponse>

  @POST("bill/tv/{customerId}")
  fun tvPayment(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("bill/xl")
  fun getXlProducts(): Single<GetCellularProductsResponse>

  @GET("bill/xl/{product}/{msisdn}")
  fun xlInquiry(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String
  ): Single<PaymentResponse>

  @POST("bill/xl/{product}/{msisdn}")
  fun xlPayment(
    @Path("product") product: String,
    @Path("msisdn") msisdn: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("creditcard/mandiri/{ccNumber}")
  fun creditCardInquiry(@Path("ccNumber") ccNumber: String): Single<PaymentResponse>

  @POST("creditcard/mandiri/{ccNumber}")
  fun creditCardPayment(
    @Path("ccNumber") ccNumber: String,
    @Body request: CreditCardPaymentRequest
  ): Single<PaymentResponse>

  @POST("emoney/emoney/{cardNumber}")
  fun eMoneyTopup(
    @Path("cardNumber") cardNumber: String,
    @Body request: CreditCardPaymentRequest
  ): Single<PaymentResponse>

  @GET("emoney/linkaja/{customerId}")
  fun linkAjaInquiry(
    @Path("customerId") customerId: String
  ): Single<PaymentResponse>

  @POST("emoney/linkaja/{customerId}")
  fun linkAjaTopup(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("emoney/gopay/{customerId}")
  fun gopayInquiry(
    @Path("customerId") customerId: String
  ): Single<PaymentResponse>

  @POST("emoney/gopay/{customerId}")
  fun gopayTopup(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @GET("emoney/ovo/{customerId}")
  fun ovoInquiry(
    @Path("customerId") customerId: String
  ): Single<PaymentResponse>

  @POST("emoney/ovo/{customerId}")
  fun ovoTopup(
    @Path("customerId") customerId: String,
    @Body request: PaymentRequest
  ): Single<PaymentResponse>

  @POST("transfer")
  fun mandiriTransfer(
    @Body request: MandiriTransferRequest
  ): Single<PaymentResponse>

  @POST("transfer/prima")
  fun primaTransfer(
    @Body request: TransferRequest
  ): Single<PaymentResponse>

  @POST("transfer/rtgs")
  fun rtgsTransfer(
    @Body request: TransferRequest
  ): Single<PaymentResponse>

  @POST("transfer/skn")
  fun sknTransfer(
    @Body request: TransferRequest
  ): Single<PaymentResponse>

}
