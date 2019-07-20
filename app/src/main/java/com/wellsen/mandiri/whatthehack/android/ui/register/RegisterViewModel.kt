/*
 * *
 *  * Created by Wellsen on 7/21/19 12:08 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/21/19 12:07 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.register

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wellsen.mandiri.whatthehack.android.R.string
import com.wellsen.mandiri.whatthehack.android.adapter.NonNullMutableLiveData
import com.wellsen.mandiri.whatthehack.android.data.model.RegisterStatus
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.request.RegisterRequest
import com.wellsen.mandiri.whatthehack.android.data.remote.response.RegisterResponse
import com.wellsen.mandiri.whatthehack.android.ui.BaseViewModel
import com.wellsen.mandiri.whatthehack.android.util.AUTHORIZATION
import com.wellsen.mandiri.whatthehack.android.util.DOB
import com.wellsen.mandiri.whatthehack.android.util.EMAIL
import com.wellsen.mandiri.whatthehack.android.util.MOTHERS_NAME
import com.wellsen.mandiri.whatthehack.android.util.NAME
import com.wellsen.mandiri.whatthehack.android.util.NIK
import com.wellsen.mandiri.whatthehack.android.util.extension.with
import com.wellsen.mandiri.whatthehack.android.util.validator.DobValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.EmailValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.NameValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.NikValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PhoneValidator
import timber.log.Timber
import kotlin.math.floor

class RegisterViewModel(
  private val sp: SharedPreferences,
  private val clientApi: ClientApi,
  private val emailValidator: EmailValidator,
  private val nikValidator: NikValidator,
  private val phoneValidator: PhoneValidator,
  private val nameValidator: NameValidator,
  private val dobValidator: DobValidator
) : BaseViewModel() {

  var pbVisibility: NonNullMutableLiveData<Int> =
    NonNullMutableLiveData(View.INVISIBLE)
  var status = MutableLiveData<RegisterStatus>()
  var name: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")
  var email: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")
  var nik: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")
  var phone: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")
  var mothersName: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")
  var dob: NonNullMutableLiveData<String> =
    NonNullMutableLiveData("")

  private val _registerForm = MutableLiveData<RegisterFormState>()
  val registerFormState: LiveData<RegisterFormState> = _registerForm

  var tempNik: String = ""

  var child: Boolean = false

  init {
    if (sp.getString(NAME, null) != null) {
      name.value = sp.getString(NAME, null)!!
    }

    if (sp.getString(EMAIL, null) != null) {
      email.value = sp.getString(EMAIL, null)!!
    }

    if (sp.getString(NIK, null) != null) {
      nik.value = sp.getString(NIK, null)!!
    }

    if (sp.getString(DOB, null) != null) {
      dob.value = sp.getString(DOB, null)!!
    }
  }

  fun onRegisterFormChanged(
    name: String,
    email: String,
    nik: String,
    phone: String,
    mothersName: String,
    dob: String
  ) {
    if (!nameValidator.isValid(name)) {
      _registerForm.value = RegisterFormState(nameError = string.invalid_name)
    } else if (!emailValidator.isValid(email)) {
      _registerForm.value = RegisterFormState(emailError = string.invalid_email)
    } else if (!phoneValidator.isValid(phone)) {
      _registerForm.value = RegisterFormState(phoneError = string.invalid_phone)
    } else if (!nameValidator.isValid(mothersName)) {
      _registerForm.value = RegisterFormState(mothersNameError = string.invalid_mothers_name)
    } else if (!dobValidator.isValid(dob)) {
      _registerForm.value = RegisterFormState(dobError = string.invalid_dob)
    } else {
      _registerForm.value = RegisterFormState(isDataValid = true)
    }
  }

  fun register(request: RegisterRequest) {
    val number = floor(Math.random() * 9_000_000_000L).toLong() + 1_000_000_000L
    tempNik = number.toString()
    request.nik = tempNik

    @Suppress("UnstableApiUsage")
    add(
      clientApi.register(request).with()
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
    Timber.d(response.message)

    sp.edit().putString(NIK, tempNik).apply()
    sp.edit().putString(NAME, name.value).apply()
    sp.edit().putString(MOTHERS_NAME, mothersName.value).apply()
    sp.edit().putString(EMAIL, email.value).apply()
    if (response.data != null) {
      sp.edit().putString(AUTHORIZATION, response.data.token).apply()
      status.value = RegisterStatus(RegisterStatus.SUCCESS)
      return
    }

    status.value = RegisterStatus(RegisterStatus.SUCCESS_ACTIVE)
  }

  private fun onRegisterError(t: Throwable) {
    Timber.e(t)
    sp.edit().remove(NAME).apply()
    sp.edit().remove(MOTHERS_NAME).apply()

    status.value = RegisterStatus(RegisterStatus.ERROR, t.localizedMessage)
  }

}
