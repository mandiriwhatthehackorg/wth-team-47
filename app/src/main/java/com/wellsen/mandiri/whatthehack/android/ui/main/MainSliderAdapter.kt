/*
 * *
 *  * Created by Wellsen on 7/20/19 11:55 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 11:54 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.main

import com.wellsen.mandiri.whatthehack.android.R
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class MainSliderAdapter : SliderAdapter() {

  override fun getItemCount(): Int {
    return 3
  }

  override fun onBindImageSlide(position: Int, viewHolder: ImageSlideViewHolder) {
    when (position) {
      0 -> viewHolder.bindImageSlide(
        R.drawable.promo_bakmigm
      )
      1 -> viewHolder.bindImageSlide(
        R.drawable.promo_pizzahut
      )
      2 -> viewHolder.bindImageSlide(
        R.drawable.promo_kfc
      )
    }
  }

}