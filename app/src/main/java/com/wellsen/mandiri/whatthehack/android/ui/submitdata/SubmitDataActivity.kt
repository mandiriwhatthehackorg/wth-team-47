/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 10:50 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.submitdata

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.data.model.Status
import com.wellsen.mandiri.whatthehack.android.databinding.ActivitySubmitDataBinding
import com.wellsen.mandiri.whatthehack.android.ui.BindingActivity
import com.wellsen.mandiri.whatthehack.android.ui.REQUEST_SUBMIT_PHOTO
import com.wellsen.mandiri.whatthehack.android.ui.submitphoto.SubmitPhotoActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class SubmitDataActivity : BindingActivity<ActivitySubmitDataBinding>() {

  @LayoutRes
  override fun getLayoutResId() = R.layout.activity_submit_data

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.vm = getViewModel()
    binding.lifecycleOwner = this

    val vm = binding.vm as SubmitDataViewModel

    vm.status.observe(this@SubmitDataActivity, Observer {

      if (it.code == Status.ERROR) {

        Toast.makeText(this@SubmitDataActivity, it.message, Toast.LENGTH_LONG).show()

      } else {

        // Proceed submit KTP
        Timber.d("Proceed submit KTP")
        startActivityForResult(Intent(this, SubmitPhotoActivity::class.java), REQUEST_SUBMIT_PHOTO)

      }

    })

    val branchCodes = ArrayList<String>()
    branchCodes.add("Cabang Area Kalimantan")

    val branchAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branchCodes)
    branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.spBranchCode.adapter = branchAdapter

    val cardTypes = ArrayList<String>()
    cardTypes.add("Mandiri Gold Regular")

    val cardAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cardTypes)
    cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.spCardType.adapter = cardAdapter

    val productTypes = ArrayList<String>()
    productTypes.add("Tabungan Regular")

    val productAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productTypes)
    productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.spProductType.adapter = productAdapter

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
      return
    }

    if (requestCode == REQUEST_SUBMIT_PHOTO) {
      setResult(Activity.RESULT_OK)
      finish()
    }
  }

}