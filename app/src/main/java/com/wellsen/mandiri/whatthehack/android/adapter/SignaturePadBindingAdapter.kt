/*
 * *
 *  * Created by Wellsen on 7/16/19 10:58 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/16/19 8:58 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.adapter

import androidx.databinding.BindingAdapter
import com.github.gcacace.signaturepad.views.SignaturePad

@BindingAdapter("onStartSigning")
fun setOnSignedListener(view: SignaturePad, onStartSigningListener: OnStartSigningListener) {
  setOnSignedListener(view, onStartSigningListener, null, null)
}

@BindingAdapter("onSigned")
fun setOnSignedListener(view: SignaturePad, onSignedListener: OnSignedListener) {
  setOnSignedListener(view, null, onSignedListener, null)
}

@BindingAdapter("onClear")
fun setOnSignedListener(view: SignaturePad, onClearListener: OnClearListener) {
  setOnSignedListener(view, null, null, onClearListener)
}

@BindingAdapter(value = ["onStartSigning", "onSigned", "onClear"], requireAll = false)
fun setOnSignedListener(
  view: SignaturePad,
  onStartSigningListener: OnStartSigningListener?,
  onSignedListener: OnSignedListener?,
  onClearListener: OnClearListener?
) {
  view.setOnSignedListener(object : SignaturePad.OnSignedListener {
    override fun onStartSigning() {
      onStartSigningListener?.onStartSigning()
    }

    override fun onSigned() {
      onSignedListener?.onSigned()
    }

    override fun onClear() {
      onClearListener?.onClear()
    }
  })
}

interface OnStartSigningListener {
  fun onStartSigning()
}

interface OnSignedListener {
  fun onSigned()
}

interface OnClearListener {
  fun onClear()
}
