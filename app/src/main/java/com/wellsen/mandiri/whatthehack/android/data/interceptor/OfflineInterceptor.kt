/*
 * *
 *  * Created by Wellsen on 7/12/19 5:28 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 5:28 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.interceptor

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.wellsen.mandiri.whatthehack.android.R
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class OfflineInterceptor(val application: Application) : Interceptor {

  override fun intercept(chain: Chain): Response {
    val builder = chain.request().newBuilder()

    if (!isNetworkConnected(application)) {
      throw IOException(application.getString(R.string.no_connection))
    }

    return chain.proceed(builder.build())
  }

}

fun isNetworkConnected(context: Context): Boolean {
  val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

  return activeNetwork?.isConnected == true
}
