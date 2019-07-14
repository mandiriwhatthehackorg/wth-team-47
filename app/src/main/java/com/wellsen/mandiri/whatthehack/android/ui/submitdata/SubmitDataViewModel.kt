/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:30 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitdata

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.R.string
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
import com.wellsen.mandiri.whatthehack.android.util.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import com.wellsen.mandiri.whatthehack.android.util.validator.NameValidator
import timber.log.Timber

class SubmitDataViewModel(
  private val adminApi: AdminApi,
  private val clientApi: ClientApi,
  private val nameValidator: NameValidator
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> = NonNullMutableLiveData(View.INVISIBLE)
  var status = MutableLiveData<Status>()
  var productTypes = ArrayList<ProductType>()
  var cardTypes = ArrayList<CardType>()
  var branchCodes = ArrayList<BranchCode>()
  var productType = NonNullMutableLiveData(ProductType(""))
  var cardType = NonNullMutableLiveData(CardType(""))
  var branchCode = NonNullMutableLiveData(BranchCode(""))
  var mothersName: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

  private val _submitDataForm = MutableLiveData<SubmitDataFormState>()
  val submitDataFormState: LiveData<SubmitDataFormState> = _submitDataForm

  init {
    getProductTypes()
    getCardTypes()
    getBranchCodes()
  }

  fun onRegisterFormChanged(mothersName: String) {
    if (!nameValidator.isValid(mothersName)) {
      _submitDataForm.value = SubmitDataFormState(mothersNameError = string.invalid_mothers_name)
    } else {
      _submitDataForm.value = SubmitDataFormState(isDataValid = true)
    }
  }

  fun submitData(request: SubmitDataRequest) {
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

  fun getProductTypes() {
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

  fun getCardTypes() {
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

  fun getBranchCodes() {
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
        mothersName.value,
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
