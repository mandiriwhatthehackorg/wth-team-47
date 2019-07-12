/*
 * *
 *  * Created by Wellsen on 7/12/19 11:26 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 11:25 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android

import android.app.Application
import com.wellsen.mandiri.whatthehack.android.module.networkModule
import com.wellsen.mandiri.whatthehack.android.module.sharedPreferenceModule
import com.wellsen.mandiri.whatthehack.android.module.validatorModule
import com.wellsen.mandiri.whatthehack.android.module.viewModelModule
import com.wellsen.mandiri.whatthehack.android.util.log.CrashReportingTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class MandiriApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree())

    startKoin {
      androidContext(this@MandiriApplication)
      if (BuildConfig.DEBUG) {
        printLogger()
      }
      modules(
        listOf(
          sharedPreferenceModule,
          networkModule,
          viewModelModule,
          validatorModule
        )
      )
    }
  }

}
