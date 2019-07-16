/*
 * *
 *  * Created by Wellsen on 7/16/19 10:58 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/14/19 8:52 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.adapter

import android.R.layout
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.wellsen.mandiri.whatthehack.android.data.model.BranchCode
import com.wellsen.mandiri.whatthehack.android.data.model.CardType
import com.wellsen.mandiri.whatthehack.android.data.model.ProductType

/**
 * fill the Spinner with all available product types.
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
    ProductTypeAdapter(
      spinner.context,
      layout.simple_spinner_dropdown_item,
      productTypes
    )
  setCurrentSelection(spinner, productType)
  setSpinnerListener(spinner, listener)
}

/**
 * fill the Spinner with all available card types.
 * Set the Spinner selection to card type.
 * If the selection changes, call the InverseBindingAdapter
 */
@BindingAdapter(
  value = ["cardTypes", "cardType", "cardTypeAttrChanged"],
  requireAll = false
)
fun setCardTypes(
  spinner: AppCompatSpinner,
  cardTypes: List<CardType>,
  cardType: CardType,
  listener: InverseBindingListener
) {
  spinner.adapter =
    CardTypeAdapter(
      spinner.context,
      layout.simple_spinner_dropdown_item,
      cardTypes
    )
  setCurrentSelection(spinner, cardType)
  setSpinnerListener(spinner, listener)
}

/**
 * fill the Spinner with all available branch codes.
 * Set the Spinner selection to branch code.
 * If the selection changes, call the InverseBindingAdapter
 */
@BindingAdapter(
  value = ["branchCodes", "branchCode", "branchCodeAttrChanged"],
  requireAll = false
)
fun setBranchCodes(
  spinner: AppCompatSpinner,
  branchCodes: List<BranchCode>,
  branchCode: BranchCode,
  listener: InverseBindingListener
) {
  spinner.adapter =
    BranchCodeAdapter(
      spinner.context,
      layout.simple_spinner_dropdown_item,
      branchCodes
    )
  setCurrentSelection(spinner, branchCode)
  setSpinnerListener(spinner, listener)
}

@InverseBindingAdapter(attribute = "productType")
fun getProductType(spinner: AppCompatSpinner): ProductType {
  return spinner.selectedItem as ProductType
}

@InverseBindingAdapter(attribute = "cardType")
fun getCardType(spinner: AppCompatSpinner): CardType {
  return spinner.selectedItem as CardType
}

@InverseBindingAdapter(attribute = "branchCode")
fun getBranchCode(spinner: AppCompatSpinner): BranchCode {
  return spinner.selectedItem as BranchCode
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

private fun setCurrentSelection(spinner: AppCompatSpinner, selectedItem: CardType): Boolean {
  for (index in 0 until spinner.adapter.count) {
    if (spinner.getItemAtPosition(index) == selectedItem.card) {
      spinner.setSelection(index)
      return true
    }
  }

  return false
}

private fun setCurrentSelection(spinner: AppCompatSpinner, selectedItem: BranchCode): Boolean {
  for (index in 0 until spinner.adapter.count) {
    if (spinner.getItemAtPosition(index) == selectedItem.branch) {
      spinner.setSelection(index)
      return true
    }
  }

  return false
}
