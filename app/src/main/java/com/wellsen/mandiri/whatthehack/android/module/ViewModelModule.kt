/*
 * *
 *  * Created by Wellsen on 7/12/19 12:17 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 12:14 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.wellsen.mandiri.whatthehack.android.ui.forgotpass.ForgotPassViewModel
import com.wellsen.mandiri.whatthehack.android.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { LoginViewModel(get(), get(), get()) }
  viewModel { ForgotPassViewModel(get()) }
}
