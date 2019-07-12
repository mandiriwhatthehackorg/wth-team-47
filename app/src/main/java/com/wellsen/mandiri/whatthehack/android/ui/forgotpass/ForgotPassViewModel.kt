/*
 * *
 *  * Created by Wellsen on 7/12/19 12:17 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 12:17 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.forgotpass

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.R.string
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.ui.login.LoginFormState
import com.wellsen.mandiri.whatthehack.android.util.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.util.validator.UsernameValidator
import timber.log.Timber

class ForgotPassViewModel(
  private val usernameValidator: UsernameValidator
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> = NonNullMutableLiveData(View.INVISIBLE)
  var username: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

  private val _forgotPassForm = MutableLiveData<LoginFormState>()
  val forgotPassFormState: LiveData<LoginFormState> = _forgotPassForm

  fun onForgotPassFormChanged(username: String) {
    if (!usernameValidator.isValid(username)) {
      _forgotPassForm.value = LoginFormState(usernameError = string.invalid_username)
    } else {
      _forgotPassForm.value = LoginFormState(isDataValid = true)
    }
  }

  fun onClick(@Suppress("unused_parameter") view: View?) {
    Timber.d("onClick Reset Password")
  }

}
