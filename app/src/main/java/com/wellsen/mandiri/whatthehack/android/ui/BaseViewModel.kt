/*
 * *
 *  * Created by Wellsen on 7/10/19 8:20 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/10/19 8:19 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

  private val disposables = CompositeDisposable()

  fun add(disposable: Disposable) {
    disposables.add(disposable)
  }

  override fun onCleared() {
    disposables.clear()
    super.onCleared()
  }

}
