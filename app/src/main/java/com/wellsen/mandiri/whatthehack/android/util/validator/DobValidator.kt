/*
 * *
 *  * Created by Wellsen on 7/12/19 3:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 2:26 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util.validator

class DobValidator(defaultErrString: String) : Validator(defaultErrString) {

  override fun isValid(text: CharSequence): Boolean {
    return text.isNotEmpty()
  }

}
