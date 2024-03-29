/*
 * *
 *  * Created by Wellsen on 7/19/19 10:50 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/19/19 9:15 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.wellsen.mandiri.whatthehack.android.data.model.CardType

class CardTypeAdapter(
  context: Context,
  textViewResourceId: Int,
  private val values: List<CardType>
) : ArrayAdapter<CardType>(context, textViewResourceId, values) {

  override fun getCount() = values.size
  override fun getItem(position: Int) = values[position]
  override fun getItemId(position: Int) = position.toLong()

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val label = super.getView(position, convertView, parent) as TextView
    label.text = values[position].cardName
    return label
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val label = super.getDropDownView(position, convertView, parent) as TextView
    label.text = values[position].cardName
    return label
  }

}
