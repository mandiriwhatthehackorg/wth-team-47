/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 11:25 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util.validator

import android.util.Patterns

class EmailValidator(defaultErrString: String) : Validator(defaultErrString) {

  override fun isValid(text: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(text).matches()
  }

}
