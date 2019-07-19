/*
 * *
 *  * Created by Wellsen on 7/19/19 11:14 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 11:05 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.wellsen.mandiri.whatthehack.android.ui.login.LoginViewModel
import com.wellsen.mandiri.whatthehack.android.ui.otp.OtpViewModel
import com.wellsen.mandiri.whatthehack.android.ui.register.RegisterViewModel
import com.wellsen.mandiri.whatthehack.android.ui.resetpass.ResetPassViewModel
import com.wellsen.mandiri.whatthehack.android.ui.submitdata.SubmitDataViewModel
import com.wellsen.mandiri.whatthehack.android.ui.submitphoto.SubmitPhotoViewModel
import com.wellsen.mandiri.whatthehack.android.ui.submitsignature.SubmitSignatureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { LoginViewModel(get(), get(), get()) }
  viewModel { ResetPassViewModel(get()) }
  viewModel { RegisterViewModel(get(), get(), get(), get(), get(), get(), get()) }
  viewModel { OtpViewModel(get(), get()) }
  viewModel { SubmitDataViewModel(get(), get(), get()) }
  viewModel { SubmitPhotoViewModel(get(), get()) }
  viewModel { SubmitSignatureViewModel(get()) }
}
