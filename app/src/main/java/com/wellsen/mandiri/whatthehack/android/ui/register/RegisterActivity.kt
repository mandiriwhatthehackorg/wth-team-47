/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 10:50 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.RegisterStatus
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityRegisterBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.EXTRA_STATUS
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_OTP
import com.wellsen.mandiri.whatthehack.android.ui.otp.OtpActivity
import com.wellsen.mandiri.whatthehack.android.util.DATE_FORMAT
import com.wellsen.mandiri.whatthehack.android.util.DOB
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class RegisterActivity : BindingActivity<ActivityRegisterBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_register

  private val sp: SharedPreferences by inject()

  private var year = 1980
  private var month = 1
  private var date = 1

  @SuppressLint("SimpleDateFormat")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    if (sp.getString(DOB, null) != null) {
      val dob = sp.getString(DOB, null)!!
      val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
      val dateTime = LocalDate.parse(dob, formatter)
      date = dateTime.dayOfMonth
      month = dateTime.monthValue
      year = dateTime.year
    }

    val vm = binding.vm as RegisterViewModel
    vm.registerFormState.observe(this@RegisterActivity, Observer {
      val registerFormState = it ?: return@Observer

      binding.tilName.error = if (registerFormState.nameError == null) null
      else getString(registerFormState.nameError)
      binding.tilEmail.error = if (registerFormState.emailError == null) null
      else getString(registerFormState.emailError)
      binding.tilNik.error = if (registerFormState.nikError == null) null
      else getString(registerFormState.nikError)
      binding.tilPhone.error = if (registerFormState.phoneError == null) null
      else getString(registerFormState.phoneError)
      binding.tilMothersName.error = if (registerFormState.mothersNameError == null) null
      else getString(registerFormState.mothersNameError)
      binding.tilDob.error = if (registerFormState.dobError == null) null
      else getString(registerFormState.dobError)

      // disable login button unless both username / password is valid
      binding.btnRegister.isEnabled = registerFormState.isDataValid
    })

    vm.status.observe(this@RegisterActivity, Observer {

      when (it.code) {
        RegisterStatus.ERROR -> Toast.makeText(
          this@RegisterActivity,
          it.message,
          Toast.LENGTH_LONG
        ).show()

        RegisterStatus.SUCCESS -> startActivityForResult(
          Intent(this, OtpActivity::class.java),
          REQUEST_OTP
        )

        RegisterStatus.SUCCESS_ACTIVE -> {
          val intent = Intent(this, OtpActivity::class.java)
          intent.putExtra(EXTRA_STATUS, true)
          startActivityForResult(intent, REQUEST_OTP)
        }
      }
    })

    setAfterTextChangedListener(binding.etName, vm)
    setAfterTextChangedListener(binding.etEmail, vm)
    setAfterTextChangedListener(binding.etNik, vm)
    setAfterTextChangedListener(binding.etPhone, vm)
    setAfterTextChangedListener(binding.etMothersName, vm)
    setAfterTextChangedListener(binding.etDob, vm)

    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
      this.date = dayOfMonth
      this.month = monthOfYear + 1
      this.year = year

      updateDobText(binding.etDob)
    }

    binding.etDob.setOnClickListener {
      DatePickerDialog(this, dateSetListener, year, month, date).show()
    }
  }

  private fun setAfterTextChangedListener(et: TextInputEditText, vm: RegisterViewModel) {
    et.afterTextChanged {
      vm.onRegisterFormChanged(
        binding.etName.text.toString(),
        binding.etEmail.text.toString(),
        binding.etNik.text.toString(),
        binding.etPhone.text.toString(),
        binding.etMothersName.text.toString(),
        binding.etDob.text.toString()
      )
    }
  }

  @SuppressLint("SetTextI18n")
  private fun updateDobText(et: TextInputEditText) {
    val yearString = year.toString()
    val monthString = if (month >= 10) month.toString() else "0$month"
    val dateString = if (date >= 10) date.toString() else "0$date"

    et.setText("$dateString-$monthString-$yearString")
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    if (requestCode == REQUEST_OTP) {
      setResult(Activity.RESULT_OK)
      finish()
    }
  }

}
