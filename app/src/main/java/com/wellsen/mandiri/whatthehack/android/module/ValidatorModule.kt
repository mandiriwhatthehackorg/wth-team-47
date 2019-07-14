/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:30 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.wellsen.mandiri.whatthehack.android.util.validator.DobValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.EmailValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.NameValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.NikValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PasswordValidator
import com.wellsen.mandiri.whatthehack.android.util.validator.PhoneValidator
import org.koin.dsl.module

val validatorModule = module {
  factory { EmailValidator("") }
  factory { NikValidator("") }
  factory { PhoneValidator("") }
  factory { DobValidator("") }
  factory { NameValidator("") }
  factory { PasswordValidator("") }
}
