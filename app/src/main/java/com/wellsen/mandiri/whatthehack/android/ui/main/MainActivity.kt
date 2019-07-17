/*
 * *
 *  * Created by Wellsen on 7/17/19 3:36 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/17/19 3:36 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.ui.login.LoginActivity
import com.wellsen.mandiri.whatthehack.android.util.LOGGED_IN
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

  private val sp: SharedPreferences by inject()

  private lateinit var textMessage: TextView
  private val onNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_home -> {
          textMessage.setText(R.string.title_home)
          return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_dashboard -> {
          textMessage.setText(R.string.title_dashboard)
          showBiometricDialog()
          return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_notifications -> {
          textMessage.setText(R.string.title_notifications)
          logout()
          return@OnNavigationItemSelectedListener true
        }
      }
      false
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val navView: BottomNavigationView = findViewById(R.id.nav_view)

    textMessage = findViewById(R.id.message)
    navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
  }

  private fun logout() {
    sp.edit().remove(LOGGED_IN).apply()
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
  }

  private fun showBiometricDialog() {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(this, executor, object : AuthenticationCallback() {

      override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
          Timber.d("User clicked negative button")
          return
        }

        Timber.e("Unrecoverable error has been encountered and the operation is complete")
        toastOnMainThread("Unrecoverable error has been encountered and the operation is complete")
      }

      override fun onAuthenticationSucceeded(result: AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        Timber.d("Authenticated, proceed")
        toastOnMainThread("Authenticated, proceed")
      }

      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Timber.d("Biometric is valid but not recognized")
        toastOnMainThread("Biometric is valid but not recognized")
      }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
      .setTitle("Title")
      .setSubtitle("Subtitle")
      .setDescription("Description")
      .setNegativeButtonText("Cancel")
      .build()

    biometricPrompt.authenticate(promptInfo)
  }

  private fun toastOnMainThread(message: String) {
    runOnUiThread {
      Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
  }

}
