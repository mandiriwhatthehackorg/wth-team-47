/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 11:25 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util.validator

class PhoneNumberValidator(defaultErrString: String) : Validator(defaultErrString) {

  override fun isValid(text: CharSequence): Boolean {
    // Ideally phone numbers should be validated via Google phonelib library.
    // This is just for demo purpose.
    val regex = "[^\\d]"
    val phoneDigits = text.toString().replace(regex.toRegex(), "")
    return phoneDigits.length >= 10
  }

}
