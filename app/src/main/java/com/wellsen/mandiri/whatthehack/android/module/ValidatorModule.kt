/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 8:44 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.wellsen.mandiri.whatthehack.android.util.validator.EmailValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PasswordValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PhoneNumberValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.UsernameValidator
import org.koin.dsl.module

val validatorModule = module {
  factory { UsernameValidator("") }
  factory { PasswordValidator("") }
  factory { EmailValidator("") }
  factory { PhoneNumberValidator("") }
}
