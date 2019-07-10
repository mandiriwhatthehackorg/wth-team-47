/*
 * *
 *  * Created by Wellsen on 7/10/19 10:16 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/10/19 10:16 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util.log

import android.util.Log
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

  /** A tree which logs important information for crash reporting. */
  override fun log(
    priority: Int,
    tag: String?,
    message: String,
    t: Throwable?
  ) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return
    }

    // log your crash to your favourite
    // Sending crash report to Firebase CrashAnalytics

    // FirebaseCrash.report(message);
    // FirebaseCrash.report(new Exception(message));

    /*FakeCrashLibrary.log(priority, tag, message)

    if (t == null) {
      return
    }

    if (priority == Log.ERROR) {
      FakeCrashLibrary.logError(t)
      return
    }

    if (priority == Log.WARN) {
      FakeCrashLibrary.logWarning(t)
    }*/

  }

}
