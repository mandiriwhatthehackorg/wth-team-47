/*
 * *
 *  * Created by Wellsen on 7/10/19 10:16 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/10/19 10:16 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android

import android.app.Application
import com.wellsen.mandiri.whatthehack.android.util.log.CrashReportingTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MandiriApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree())

    startKoin {
      androidContext(this@MandiriApplication)
    }
  }

}
