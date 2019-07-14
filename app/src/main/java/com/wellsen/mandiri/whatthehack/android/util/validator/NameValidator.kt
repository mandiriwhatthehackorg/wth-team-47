/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 7:30 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util.validator

class NameValidator(defaultErrString: String) : Validator(defaultErrString) {

  override fun isValid(text: CharSequence): Boolean {
    return text.length in 4..255
  }

}
