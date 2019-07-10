/*
 * *
 *  * Created by Wellsen on 7/10/19 10:38 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/10/19 10:38 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import com.google.gson.GsonBuilder
import com.wellsen.mandiri.whatthehack.android.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

  single { GsonBuilder().create() }

  single {
    OkHttpClient.Builder()
        .apply {
          cache(get())
          connectTimeout(10, TimeUnit.SECONDS)
          writeTimeout(10, TimeUnit.SECONDS)
          readTimeout(10, TimeUnit.SECONDS)
          if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
              level = HttpLoggingInterceptor.Level.BODY
            })
          }
        }
        .build()
  }

  single(named(BuildConfig.ONBOARDING_BASE_URL)) {
    Retrofit.Builder()
        .baseUrl(BuildConfig.ONBOARDING_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(get()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(get())
        .build()
  }

  single(named(BuildConfig.API_GATEWAY_BASE_URL)) {
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_GATEWAY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(get()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(get())
        .build()
  }

}
