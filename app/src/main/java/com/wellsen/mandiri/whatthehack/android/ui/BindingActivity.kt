/*
 * *
 *  * Created by Wellsen on 7/10/19 8:18 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/10/19 1:37 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingActivity<T : ViewDataBinding> : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    protected lateinit var binding: T private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayoutResId())
    }

}
