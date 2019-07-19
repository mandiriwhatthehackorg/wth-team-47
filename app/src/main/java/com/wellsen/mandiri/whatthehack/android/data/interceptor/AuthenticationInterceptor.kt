/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 7:38 PM
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

    val bearer = sp.getString(AUTHORIZATION, null)
    requestBuilder.addHeader("Authorization", "Bearer $bearer")

    return chain.proceed(requestBuilder.build())
  }

}
