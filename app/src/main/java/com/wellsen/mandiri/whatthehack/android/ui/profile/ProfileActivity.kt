/*
 * *
 *  * Created by Wellsen on 7/20/19 10:07 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 10:07 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.ui.login.LoginActivity
import com.wellsen.mandiri.whatthehack.android.ui.scanner.QrCodeScannerActivity
import com.wellsen.mandiri.whatthehack.android.util.PHOTO_URL
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject

class ProfileActivity : AppCompatActivity() {

  private val sp: SharedPreferences by inject()

  lateinit var civProfile: CircleImageView
  lateinit var btnLogout: TextView
  lateinit var ivCard: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    civProfile = findViewById(R.id.civ_profile)
    btnLogout = findViewById(R.id.btn_logout)
    ivCard = findViewById(R.id.iv_card)

    btnLogout.setOnClickListener {
      startActivity(Intent(this, LoginActivity::class.java))
      sp.edit().clear().apply()
      finish()
    }

    ivCard.setOnClickListener {
      startActivity(Intent(this, QrCodeScannerActivity::class.java))
    }

    val photoUrl = sp.getString(PHOTO_URL, null)
    if (photoUrl != null) {
      Glide.with(this)
        .load(photoUrl)
        .fitCenter()
        .into(civProfile)
    }

  }

}
