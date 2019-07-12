/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 9:14 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util.validator

/**
 * Base Validator class to either implement or inherit from for custom validation
 */
abstract class Validator(
  /**
   * Error message that the view will display if validation fails.
   *
   *
   * This is protected, so you can change this dynamically in your [.isValid]
   * implementation. If necessary, you can also interact with this via its getter and setter.
   */
  val errorMessage: String
) {

  /**
   * Abstract method to implement your own validation checking.
   *
   * @param text    The CharSequence representation of the text in the EditText field. Cannot be null, but may be empty.
   * @return True if valid, false if not
   */
  abstract fun isValid(text: CharSequence): Boolean

}
