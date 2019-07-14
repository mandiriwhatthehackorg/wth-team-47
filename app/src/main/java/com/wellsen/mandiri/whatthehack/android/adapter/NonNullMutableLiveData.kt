/*
 * *
 *  * Created by Wellsen on 7/14/19 8:53 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:53 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.adapter

import androidx.lifecycle.MutableLiveData

class NonNullMutableLiveData<T : Any>(defaultValue: T) : MutableLiveData<T>() {

  init {
    value = defaultValue
  }

  override fun getValue() = super.getValue()!!

}
