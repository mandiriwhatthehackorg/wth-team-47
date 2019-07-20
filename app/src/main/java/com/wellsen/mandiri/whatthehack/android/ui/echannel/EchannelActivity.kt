/*
 * *
 *  * Created by Wellsen on 7/20/19 8:20 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 8:08 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.echannel

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import com.google.android.material.textfield.TextInputEditText
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.ui.main.REQUEST_LOCK_CODE
import com.wellsen.mandiri.whatthehack.android.ui.main.REQUEST_SECURITY_SETTINGS
import com.wellsen.mandiri.whatthehack.android.util.isFingerprintAvailable
import com.wellsen.mandiri.whatthehack.android.util.isHardwareSupported
import timber.log.Timber
import java.util.concurrent.Executors

class EchannelActivity : AppCompatActivity() {

  private lateinit var keyguardManager: KeyguardManager

  private lateinit var etNominal: TextInputEditText
  private lateinit var tvBalance: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_echannel)

    keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    etNominal = findViewById(R.id.et_nominal)
    tvBalance = findViewById(R.id.tv_balance)

    findViewById<TextView>(R.id.btn_topup).setOnClickListener {
      checkBiometric("Topup LinkAja", "")
    }

  }

  private fun proceed() {
    val balance = etNominal.text.toString().toLong() + 250000
    runOnUiThread {
      tvBalance.text = "IDR $balance"
    }

    finish()
  }

  private fun checkBiometric(title: String, description: String) {
    if (!keyguardManager.isDeviceSecure) {
      Toast.makeText(
        this@EchannelActivity,
        "You need to activate PIN, pattern or password first", Toast.LENGTH_LONG
      ).show()
      val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
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
        this@EchannelActivity,
        "You need to activate PIN, pattern or password first", Toast.LENGTH_LONG
      ).show()
      //If some exception occurs means Screen lock is not set up please set screen lock
      //Open Security screen directly to enable patter lock
      val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
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
      Toast.makeText(this@EchannelActivity, message, Toast.LENGTH_LONG).show()
    }
  }

}
