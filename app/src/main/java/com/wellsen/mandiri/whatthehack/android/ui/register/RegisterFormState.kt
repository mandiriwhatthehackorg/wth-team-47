/*
 * *
 *  * Created by Wellsen on 7/12/19 3:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 2:17 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.register

data class RegisterFormState(

  val emailError: Int? = null,
  val nikError: Int? = null,
  val phoneError: Int? = null,
  val dobError: Int? = null,
  val isDataValid: Boolean = false

)
