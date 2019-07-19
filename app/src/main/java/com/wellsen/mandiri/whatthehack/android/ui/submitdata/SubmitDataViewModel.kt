/*
 * *
 *  * Created by Wellsen on 7/19/19 11:14 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 11:03 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitdata

import android.content.SharedPreferences
import android.net.Uri
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
import com.wellsen.mandiri.whatthehack.android.data.remote.response.SubmitKtpResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.AUTHORIZATION
import com.wellsen.mandiri.whatthehack.android.util.KTP_FILE
import com.wellsen.mandiri.whatthehack.android.util.MOTHERS_NAME
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

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
    NonNullMutableLiveData(ProductType("", ""))
  var cardType =
    NonNullMutableLiveData(CardType("gold", "Mandiri Gold Regular"))
  var branchCode =
    NonNullMutableLiveData(BranchCode("76548", "Cabang Area Kalimantan", "Kalimantan"))

  lateinit var ktpFile: File

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
          { onSubmitDataError(it) }
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

  private fun submitKtp() {
    ktpFile = File(Uri.parse(sp.getString(KTP_FILE, null)!!).path!!)
    val fileBody = ktpFile.asRequestBody("image/*".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("file", ktpFile.name, fileBody)

    @Suppress("UnstableApiUsage")
    add(
      clientApi.submitKtp(body).with()
        .doOnSubscribe { onRequestStart() }
        .doOnTerminate { onResponseFinish() }
        .subscribe(
          { onSubmitKtpSuccess(it) },
          { onResponseError(it) }
        )
    )
  }

  fun onClickSubmit(@Suppress("unused_parameter") view: View?) {
    submitData(
      SubmitDataRequest(
        "TAB",
        "gold",
        sp.getString(MOTHERS_NAME, "") ?: "",
        "76548"
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
    Timber.d(response.message)
    sp.edit().putString(AUTHORIZATION, response.data.token).apply()
    submitKtp()
  }

  private fun onSubmitKtpSuccess(response: SubmitKtpResponse) {
    Timber.d(response.message)
    sp.edit().putString(AUTHORIZATION, response.data.token).apply()
    status.value = Status(Status.SUCCESS)
  }

  private fun onGetProductTypesSuccess(response: GetProductTypesResponse) {
    Timber.d(response.toString())
    productTypes = response.data.data as ArrayList<ProductType>
  }

  private fun onGetCardTypesSuccess(response: GetCardTypesResponse) {
    Timber.d(response.toString())
    cardTypes = response.data.data as ArrayList<CardType>
  }

  private fun onGetBranchCodesSuccess(response: GetBranchCodesResponse) {
    Timber.d(response.toString())
    branchCodes = response.data.data as ArrayList<BranchCode>
  }

  private fun onResponseError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
  }

  private fun onSubmitDataError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
    submitKtp()
  }

}
