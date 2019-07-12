/*
 * *
 *  * Created by Wellsen on 7/12/19 4:54 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 4:52 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.model

data class Status(

  val code: Int,
  val message: String = ""

) {

  companion object {

    const val SUCCESS = 2
    const val ERROR = 4

  }

}
