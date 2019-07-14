/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:30 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.request.LoginRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.LoginResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import com.wellsen.mandiri.whatthehack.android.util.validator.NameValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PasswordValidator
import timber.log.Timber

class LoginViewModel(
  private val clientApi: ClientApi,
  private val nameValidator: NameValidator,
  private val passwordValidator: PasswordValidator
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> = NonNullMutableLiveData(View.INVISIBLE)
  var username: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var password: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var status = MutableLiveData<Status>()

  private val _loginForm = MutableLiveData<LoginFormState>()
  val loginFormState: LiveData<LoginFormState> = _loginForm

  fun onLoginFormChanged(username: String, password: String) {
    if (!nameValidator.isValid(username)) {
      _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
    } else if (!passwordValidator.isValid(password)) {
      _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
    } else {
      _loginForm.value = LoginFormState(isDataValid = true)
    }
  }

  fun login(request: LoginRequest) {
    @Suppress("UnstableApiUsage")
    add(
      clientApi.login(request).with()
      .doOnSubscribe { onLoginStart() }
      .doOnTerminate { onLoginFinish() }
      .subscribe(
        { onLoginSuccess(it) },
        { onLoginError(it) }
      )
    )
  }

  fun onClick(@Suppress("unused_parameter") view: View?) {
    login(LoginRequest(username.value, password.value))
  }

  private fun onLoginStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onLoginFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onLoginSuccess(response: LoginResponse) {
    Timber.d(response.response)
    status.value = Status(Status.SUCCESS)
  }

  private fun onLoginError(t: Throwable) {
    Timber.e(t)
    status.value = Status(Status.ERROR, t.localizedMessage)
  }

}
