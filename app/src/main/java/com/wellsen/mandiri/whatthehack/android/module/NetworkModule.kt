/*
 * *
 *  * Created by Wellsen on 7/12/19 5:28 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/12/19 5:26 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.google.gson.GsonBuilder
import com.wellsen.mandiri.whatthehack.android.BuildConfig
import com.wellsen.mandiri.whatthehack.android.data.interceptor.AuthenticationInterceptor
import com.wellsen.mandiri.whatthehack.android.data.interceptor.OfflineInterceptor
import com.wellsen.mandiri.whatthehack.android.data.remote.api.Api
import com.wellsen.mandiri.whatthehack.android.data.remote.api.OnboardingApi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

  single { GsonBuilder().create() }

  single { OfflineInterceptor(androidApplication()) }

  single { AuthenticationInterceptor(get()) }

  single {
    OkHttpClient.Builder()
      .apply {
        connectTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        addInterceptor(get<OfflineInterceptor>())
        addInterceptor(get<AuthenticationInterceptor>())
        if (BuildConfig.DEBUG) {
          addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
          })
        }
      }
      .build()
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.ONBOARDING_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(OnboardingApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(Api::class.java)
  }

}
