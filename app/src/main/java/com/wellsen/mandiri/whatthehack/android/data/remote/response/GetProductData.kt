/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 9:20 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.data.remote.response

import com.wellsen.mandiri.whatthehack.android.data.model.ProductType

data class GetProductData(

  val viewName: String,
  val token: String,
  val data: List<ProductType>

)
