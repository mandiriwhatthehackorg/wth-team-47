/*
 * *
 *  * Created by Wellsen on 7/20/19 6:18 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 6:18 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.main

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.ui.echannel.EchannelActivity
import com.wellsen.mandiri.whatthehack.android.ui.login.LoginActivity
import com.wellsen.mandiri.whatthehack.android.util.LOGGED_IN
import com.wellsen.mandiri.whatthehack.android.util.isFingerprintAvailable
import com.wellsen.mandiri.whatthehack.android.util.isHardwareSupported
import org.koin.android.ext.android.inject
import ss.com.bannerslider.Slider
import timber.log.Timber
import java.util.concurrent.Executors

const val REQUEST_LOCK_CODE = 200
const val REQUEST_SECURITY_SETTINGS = 201

const val EXTRA_CHANNEL = "channel"
const val CHANNEL_LINKAJA = "link_aja"
const val CHANNEL_GOPAY = "gopay"
const val CHANNEL_OVO = "ovo"

class MainActivity : AppCompatActivity() {

  private lateinit var keyguardManager: KeyguardManager
  private val sp: SharedPreferences by inject()

  private val onNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_home -> {

          return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_dashboard -> {

          checkBiometric("Title", "Description")
          return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_notifications -> {

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
    keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    Slider.init(GlideImageLoadingService(this))

    navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    val slider = findViewById<Slider>(R.id.banner_slider1)
    slider.setAdapter(MainSliderAdapter())
    slider.setInterval(5000)

    findViewById<AppCompatImageView>(R.id.iv_linkaja).setOnClickListener {
      val intent = Intent(this, EchannelActivity::class.java)
      intent.putExtra(EXTRA_CHANNEL, CHANNEL_LINKAJA)
      startActivity(intent)
    }

    findViewById<AppCompatImageView>(R.id.iv_gopay).setOnClickListener {
      intent.putExtra(EXTRA_CHANNEL, CHANNEL_GOPAY)
      startActivity(intent)
    }

    findViewById<AppCompatImageView>(R.id.iv_ovo).setOnClickListener {
      intent.putExtra(EXTRA_CHANNEL, CHANNEL_OVO)
      startActivity(intent)
    }
  }

  private fun logout() {
    sp.edit().remove(LOGGED_IN).apply()
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
  }

  private fun checkBiometric(title: String, description: String) {
    if (!keyguardManager.isDeviceSecure) {
      Toast.makeText(
        this@MainActivity,
        "You need to activate PIN, pattern or password first", Toast.LENGTH_LONG
      ).show()
      val intent = Intent(ACTION_SECURITY_SETTINGS)
      try {
        startActivityForResult(intent, REQUEST_SECURITY_SETTINGS)
      } catch (ex: Exception) {
        Timber.e(ex.localizedMessage)
      }
      return
    }

    if (!isHardwareSupported(this)) {
      Timber.d("Fingerprint not supported on this device")
      showKeyguard(title, description)
      return
    }

    if (!isFingerprintAvailable(this)) {
      Timber.d("Fingerprint not available on this device")
      showKeyguard(title, description)
      return
    }

    showBiometricDialog(title, description)
  }

  private fun showKeyguard(title: String, description: String) {
    val i = keyguardManager.createConfirmDeviceCredentialIntent(title, description)
    try {
      startActivityForResult(i, REQUEST_LOCK_CODE)
    } catch (e: Exception) {
      //If some exception occurs means Screen lock is not set up please set screen lock
      Timber.e(e.localizedMessage)
      Toast.makeText(
        this@MainActivity,
        "You need to activate PIN, pattern or password first", Toast.LENGTH_LONG
      ).show()
      //If some exception occurs means Screen lock is not set up please set screen lock
      //Open Security screen directly to enable patter lock
      val intent = Intent(ACTION_SECURITY_SETTINGS)
      try {
        startActivityForResult(intent, REQUEST_SECURITY_SETTINGS)
      } catch (ex: Exception) {
        Timber.e(ex.localizedMessage)
      }

    }
  }

  private fun showBiometricDialog(title: String, description: String) {
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
        proceed()
      }

      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Timber.d("Biometric is valid but not recognized")
        toastOnMainThread("Biometric is valid but not recognized")
      }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
      .setTitle(title)
      .setDescription(description)
      .setNegativeButtonText("Cancel")
      .build()

    biometricPrompt.authenticate(promptInfo)
  }

  private fun toastOnMainThread(message: String) {
    runOnUiThread {
      Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
  }

  private fun proceed() {
    Timber.d("Proceed")
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == REQUEST_LOCK_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        Timber.d("Authenticated, proceed")
        proceed()
        return
      }

      Timber.d("Not Authenticated")
      Toast.makeText(
        this@MainActivity,
        "Authentication failed", Toast.LENGTH_LONG
      ).show()
    }
  }

}
