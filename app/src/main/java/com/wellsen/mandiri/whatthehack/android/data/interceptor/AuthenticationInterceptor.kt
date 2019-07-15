/*
 * *
 *  * Created by Wellsen on 7/15/19 2:37 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/15/19 2:30 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.interceptor

import android.content.SharedPreferences
import com.wellsen.mandiri.whatthehack.android.util.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class AuthenticationInterceptor(private val sp: SharedPreferences) : Interceptor {

  override fun intercept(chain: Chain): Response {
    val request = chain.request()
    val requestBuilder = request.newBuilder()

    if (sp.getString(AUTHORIZATION, null) == null) {
      return chain.proceed(request)
    }

    requestBuilder.addHeader("Authorization", sp.getString(AUTHORIZATION, null)!!)

    return chain.proceed(requestBuilder.build())
  }

}
