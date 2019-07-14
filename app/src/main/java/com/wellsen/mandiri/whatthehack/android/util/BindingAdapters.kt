/*
 * *
 *  * Created by Wellsen on 7/14/19 8:30 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:30 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.util

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.wellsen.mandiri.whatthehack.android.data.model.ProductType

/**
 * fill the Spinner with all available product type.
 * Set the Spinner selection to product type.
 * If the selection changes, call the InverseBindingAdapter
 */
@BindingAdapter(
  value = ["productTypes", "productType", "productTypeAttrChanged"],
  requireAll = false
)
fun setProductTypes(
  spinner: AppCompatSpinner,
  productTypes: List<ProductType>,
  productType: ProductType,
  listener: InverseBindingListener
) {
  spinner.adapter =
    NameAdapter(spinner.context, android.R.layout.simple_spinner_dropdown_item, productTypes)
  setCurrentSelection(spinner, productType)
  setSpinnerListener(spinner, listener)
}

@InverseBindingAdapter(attribute = "productType")
fun getProductType(spinner: AppCompatSpinner): ProductType {
  return spinner.selectedItem as ProductType
}

private fun setSpinnerListener(spinner: AppCompatSpinner, listener: InverseBindingListener) {
  spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      listener.onChange()

    override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
  }
}

private fun setCurrentSelection(spinner: AppCompatSpinner, selectedItem: ProductType): Boolean {
  for (index in 0 until spinner.adapter.count) {
    if (spinner.getItemAtPosition(index) == selectedItem.product) {
      spinner.setSelection(index)
      return true
    }
  }

  return false
}
