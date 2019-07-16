/*
 * *
 *  * Created by Wellsen on 7/16/19 7:21 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 6:43 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.register

data class RegisterFormState(

  val nameError: Int? = null,
  val emailError: Int? = null,
  val nikError: Int? = null,
  val phoneError: Int? = null,
  val mothersNameError: Int? = null,
  val dobError: Int? = null,
  val isDataValid: Boolean = false

)
