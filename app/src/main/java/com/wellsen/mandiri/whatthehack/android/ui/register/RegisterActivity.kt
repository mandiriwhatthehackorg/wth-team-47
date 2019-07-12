/*
 * *
 *  * Created by Wellsen on 7/12/19 3:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 3:54 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivityRegisterBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.util.extension.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RegisterActivity : BindingActivity<ActivityRegisterBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_register

  private var year = 1980
  private var month = 1
  private var date = 1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as RegisterViewModel
    vm.registerFormState.observe(this@RegisterActivity, Observer {
      val registerFormState = it ?: return@Observer

      binding.tilEmail.error = if (registerFormState.emailError == null) null
      else getString(registerFormState.emailError)
      binding.tilNik.error = if (registerFormState.nikError == null) null
      else getString(registerFormState.nikError)
      binding.tilPhone.error = if (registerFormState.phoneError == null) null
      else getString(registerFormState.phoneError)
      binding.tilDob.error = if (registerFormState.dobError == null) null
      else getString(registerFormState.dobError)

      // disable login button unless both username / password is valid
      binding.btnRegister.isEnabled = registerFormState.isDataValid
    })

    vm.error.observe(this@RegisterActivity, Observer {
      Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_LONG).show()
    })

    setAfterTextChangedListener(binding.etEmail, vm)
    setAfterTextChangedListener(binding.etNik, vm)
    setAfterTextChangedListener(binding.etPhone, vm)
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
        binding.etEmail.text.toString(),
        binding.etNik.text.toString(),
        binding.etPhone.text.toString(),
        binding.etDob.text.toString()
      )
    }
  }

  @SuppressLint("SetTextI18n")
  private fun updateDobText(et: TextInputEditText) {
    val yearString = year.toString()
    val monthString = if (month >= 10) month.toString() else "0$month"
    val dateString = if (date >= 10) date.toString() else "0$date"

    et.setText("$yearString-$monthString-$dateString")
  }

}
