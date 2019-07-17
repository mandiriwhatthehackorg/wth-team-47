/*
 * *
 *  * Created by Wellsen on 7/17/19 12:04 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 12:00 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.module

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import com.wellsen.mandiri.whatthehack.android.BuildConfig
import com.wellsen.mandiri.whatthehack.android.data.interceptor.AuthenticationInterceptor
import com.wellsen.mandiri.whatthehack.android.data.interceptor.OfflineInterceptor
import com.wellsen.mandiri.whatthehack.android.data.remote.api.AdminApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ClientApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.CustomerExperienceApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.MarketingApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ProductApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.SalesApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.ServicingApi
import com.wellsen.mandiri.whatthehack.android.data.remote.api.TransactionApi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

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
        sslSocketFactory(get(), get<Array<TrustManager>>()[0] as X509TrustManager)
        hostnameVerifier(get())
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
    arrayOf<TrustManager>(object : X509TrustManager {
      @SuppressLint("TrustAllX509TrustManager")
      @Throws(CertificateException::class)
      override fun checkClientTrusted(
        chain: Array<java.security.cert.X509Certificate>,
        authType: String
      ) {
      }

      @SuppressLint("TrustAllX509TrustManager")
      @Throws(CertificateException::class)
      override fun checkServerTrusted(
        chain: Array<java.security.cert.X509Certificate>,
        authType: String
      ) {
      }

      override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
        return arrayOf()
      }
    })
  }

  single {
    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, get(), java.security.SecureRandom())
    // Create an ssl socket factory with our all-trusting manager
    sslContext.socketFactory
  }

  single {
    HostnameVerifier { _, _ -> true }
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.ONBOARDING_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(ClientApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.ONBOARDING_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(AdminApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL + "CustomerExpAPI/1.0/")
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(CustomerExperienceApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL + "MarketingAPI/1.0/")
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(MarketingApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL + "ProductAPI/1.0/")
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(ProductApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL + "ServicingAPI/1.0/")
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(ServicingApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL + "TrxAndPaymentAPI/1.0/")
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(TransactionApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_GATEWAY_BASE_URL + "SalesAPI/1.0/")
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .client(get())
      .build()
      .create(SalesApi::class.java)
  }

}
