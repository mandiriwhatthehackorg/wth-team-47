/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/11/19 1:46 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util

import androidx.lifecycle.MutableLiveData

class NonNullMutableLiveData<T : Any>(defaultValue: T) : MutableLiveData<T>() {

  init {
    value = defaultValue
  }

  override fun getValue() = super.getValue()!!

}
