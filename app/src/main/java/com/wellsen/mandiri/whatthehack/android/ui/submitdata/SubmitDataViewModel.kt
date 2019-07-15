/*
 * *
 *  * Created by Wellsen on 7/15/19 2:53 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/15/19 2:43 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitdata

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.adapter.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.data.model.BranchCode
import com.wellsen.mandiri.whatthehack.android.data.model.CardType
import com.wellsen.mandiri.whatthehack.android.data.model.ProductType
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.data.remote.api.AdminApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.request.SubmitDataRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetBranchCodesResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetCardTypesResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.GetProductTypesResponse
import com.wellsen.mandiri.whatthehack.android.data.remote.response.SubmitDataResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.MOTHERS_NAME
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import timber.log.Timber

class SubmitDataViewModel(
  private val sp: SharedPreferences,
  private val adminApi: AdminApi,
  private val clientApi: ClientApi
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var status = MutableLiveData<Status>()
  var productTypes = ArrayList<ProductType>()
  var cardTypes = ArrayList<CardType>()
  var branchCodes = ArrayList<BranchCode>()
  var productType =
    NonNullMutableLiveData(ProductType(""))
  var cardType =
    NonNullMutableLiveData(CardType(""))
  var branchCode =
    NonNullMutableLiveData(BranchCode(""))

  init {
    getProductTypes()
    getCardTypes()
    getBranchCodes()
  }

  private fun submitData(request: SubmitDataRequest) {
    @Suppress("UnstableApiUsage")
    add(
      clientApi.submitData(request).with()
        .doOnSubscribe { onRequestStart() }
        .doOnTerminate { onResponseFinish() }
        .subscribe(
          { onSubmitDataSuccess(it) },
          { onResponseError(it) }
        )
    )
  }

  private fun getProductTypes() {
    @Suppress("UnstableApiUsage")
    add(
      adminApi.getProductTypes().with()
        .doOnSubscribe { onRequestStart() }
        .doOnTerminate { onResponseFinish() }
        .subscribe(
          { onGetProductTypesSuccess(it) },
          { onResponseError(it) }
        )
    )
  }

  private fun getCardTypes() {
    @Suppress("UnstableApiUsage")
    add(
      adminApi.getCardTypes().with()
        .doOnSubscribe { onRequestStart() }
        .doOnTerminate { onResponseFinish() }
        .subscribe(
          { onGetCardTypesSuccess(it) },
          { onResponseError(it) }
        )
    )
  }

  private fun getBranchCodes() {
    @Suppress("UnstableApiUsage")
    add(
      adminApi.getBranchCodes().with()
        .doOnSubscribe { onRequestStart() }
        .doOnTerminate { onResponseFinish() }
        .subscribe(
          { onGetBranchCodesSuccess(it) },
          { onResponseError(it) }
        )
    )
  }

  fun onClickSubmit(@Suppress("unused_parameter") view: View?) {
    submitData(
      SubmitDataRequest(
        productType.value.product,
        cardType.value.card,
        sp.getString(MOTHERS_NAME, "") ?: "",
        branchCode.value.branch
      )
    )
  }

  private fun onRequestStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onResponseFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onSubmitDataSuccess(response: SubmitDataResponse) {
    Timber.d(response.response)
    status.value = Status(Status.SUCCESS)
  }

  private fun onGetProductTypesSuccess(response: GetProductTypesResponse) {
    Timber.d(response.toString())
    productTypes = response.products as ArrayList<ProductType>
  }

  private fun onGetCardTypesSuccess(response: GetCardTypesResponse) {
    Timber.d(response.toString())
    cardTypes = response.cards as ArrayList<CardType>
  }

  private fun onGetBranchCodesSuccess(response: GetBranchCodesResponse) {
    Timber.d(response.toString())
    branchCodes = response.branchCodes as ArrayList<BranchCode>
  }

  private fun onResponseError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
  }

}
