/*
 * *
 *  * Created by Wellsen on 7/11/19 12:52 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/11/19 12:51 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.splash

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySplashBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.login.LoginActivity

class SplashActivity : BindingActivity<ActivitySplashBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_splash

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.lifecycleOwner = this

    val logo = findViewById<View>(R.id.iv_logo)

    Handler().postDelayed(
        {
          startActivity(
              Intent(this, LoginActivity::class.java),
              ActivityOptions.makeSceneTransitionAnimation(
                  this,
                  logo, "logo"
              ).toBundle()
          )
          finish()
        },
        3000
    )
  }

}
