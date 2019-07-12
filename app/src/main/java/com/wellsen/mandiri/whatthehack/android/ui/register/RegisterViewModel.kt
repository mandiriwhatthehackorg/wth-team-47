/*
 * *
 *  * Created by Wellsen on 7/12/19 3:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 3:54 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.register

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.R.string
import com.wellsen.mandiri.whatthehack.android.data.remote.api.OnboardingApi
import com.wellsen.mandiri.whatthehack.android.data.remote.request.RegisterRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.RegisterResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import com.wellsen.mandiri.whatthehack.android.util.validator.DobValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.EmailValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.NikValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PhoneValidator
import timber.log.Timber

class RegisterViewModel(
  private val onboardingApi: OnboardingApi,
  private val emailValidator: EmailValidator,
  private val nikValidator: NikValidator,
  private val phoneValidator: PhoneValidator,
  private val dobValidator: DobValidator
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> = NonNullMutableLiveData(View.INVISIBLE)
  var error = MutableLiveData<String>()
  var email: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var nik: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var phone: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
  var dob: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

  private val _registerForm = MutableLiveData<RegisterFormState>()
  val registerFormState: LiveData<RegisterFormState> = _registerForm

  fun onRegisterFormChanged(email: String, nik: String, phone: String, dob: String) {
    if (!emailValidator.isValid(email)) {
      _registerForm.value = RegisterFormState(emailError = string.invalid_email)
    } else if (!nikValidator.isValid(nik)) {
      _registerForm.value = RegisterFormState(nikError = string.invalid_nik)
    } else if (!phoneValidator.isValid(phone)) {
      _registerForm.value = RegisterFormState(phoneError = string.invalid_phone)
    } else if (!dobValidator.isValid(dob)) {
      _registerForm.value = RegisterFormState(dobError = string.invalid_dob)
    } else {
      _registerForm.value = RegisterFormState(isDataValid = true)
    }
  }

  fun register(request: RegisterRequest) {
    @Suppress("UnstableApiUsage")
    add(onboardingApi.register(request).with()
      .doOnSubscribe { onRegisterStart() }
      .doOnTerminate { onRegisterFinish() }
      .subscribe(
        { onRegisterSuccess(it) },
        { onRegisterError(it) }
      )
    )
  }

  fun onClickRegister(@Suppress("unused_parameter") view: View?) {
    register(RegisterRequest(email.value, nik.value, phone.value, dob.value))
  }

  private fun onRegisterStart() {
    pbVisibility.value = View.VISIBLE
  }

  private fun onRegisterFinish() {
    pbVisibility.value = View.GONE
  }

  private fun onRegisterSuccess(response: RegisterResponse) {
    Timber.d(response.response)
  }

  private fun onRegisterError(t: Throwable) {
    Timber.e(t)
    error.value = t.localizedMessage
  }

}
