/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 10:06 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

data class LoginFormState(
  val usernameError: Int? = null,
  val passwordError: Int? = null,
  val isDataValid: Boolean = false
)
