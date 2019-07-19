/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 8:34 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.model

data class RegisterStatus(

  val code: Int,
  val message: String = ""

) {

  companion object {

    const val SUCCESS = 2
    const val SUCCESS_ACTIVE = 21
    const val ERROR = 4

  }

}
