/*
 * *
 *  * Created by Wellsen on 7/10/19 10:38 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/10/19 10:21 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import android.preference.PreferenceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val sharedPreferenceModule = module {
  single { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }
}
