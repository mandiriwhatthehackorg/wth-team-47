/*
 * *
 *  * Created by Wellsen on 7/20/19 4:08 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 4:07 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.scanner

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrCodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
  private var mScannerView: ZXingScannerView? = null

  public override fun onCreate(state: Bundle?) {
    super.onCreate(state)
    mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
    setContentView(mScannerView)                // Set the scanner view as the content view
  }

  public override fun onResume() {
    super.onResume()
    mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
    mScannerView!!.startCamera()          // Start camera on resume
  }

  public override fun onPause() {
    super.onPause()
    mScannerView!!.stopCamera()           // Stop camera on pause
  }

  override fun handleResult(rawResult: Result) {
    // Do something with the result here
    rawResult.barcodeFormat.toString()
    setResult(Activity.RESULT_OK)
    finish()
  }

}