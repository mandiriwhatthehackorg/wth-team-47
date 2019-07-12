/*
 * *
 *  * Created by Wellsen on 7/12/19 3:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 2:15 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.login

data class LoginFormState(

  val usernameError: Int? = null,
  val passwordError: Int? = null,
  val isDataValid: Boolean = false

)
