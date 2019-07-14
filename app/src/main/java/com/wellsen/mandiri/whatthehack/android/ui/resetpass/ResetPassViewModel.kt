/*
 * *
 *  * Created by Wellsen on 7/14/19 8:52 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:52 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.resetpass

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.R.string
import com.wellsen.mandiri.whatthehack.android.adapter.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.validator.NameValidator
import timber.log.Timber

class ResetPassViewModel(
  private val nameValidator: NameValidator
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var username: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")

  private val _resetPassForm = MutableLiveData<ResetPassFormState>()
  val resetPassFormState: LiveData<ResetPassFormState> = _resetPassForm

  fun onForgotPassFormChanged(username: String) {
    if (!nameValidator.isValid(username)) {
      _resetPassForm.value = ResetPassFormState(usernameError = string.invalid_username)
    } else {
      _resetPassForm.value = ResetPassFormState(isDataValid = true)
    }
  }

  fun onClick(@Suppress("unused_parameter") view: View?) {
    Timber.d("onClick Reset Password")
  }

}
