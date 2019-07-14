/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:08 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.wellsen.mandiri.whatthehack.android.ui.login.LoginViewModel
import com.wellsen.mandiri.whatthehack.android.ui.onboarding.OnboardingViewModel
import com.wellsen.mandiri.whatthehack.android.ui.otp.OtpViewModel
import com.wellsen.mandiri.whatthehack.android.ui.register.RegisterViewModel
import com.wellsen.mandiri.whatthehack.android.ui.resetpass.ResetPassViewModel
import com.wellsen.mandiri.whatthehack.android.ui.submitdata.SubmitDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { LoginViewModel(get(), get(), get()) }
  viewModel { ResetPassViewModel(get()) }
  viewModel { RegisterViewModel(get(), get(), get(), get(), get()) }
  viewModel { OtpViewModel(get()) }
  viewModel { SubmitDataViewModel(get(), get(), get()) }
  viewModel { OnboardingViewModel() }
}
