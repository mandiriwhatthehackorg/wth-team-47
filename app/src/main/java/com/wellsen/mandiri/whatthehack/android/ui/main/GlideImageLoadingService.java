/*
 * *
 *  * Created by Wellsen on 7/20/19 11:55 AM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 11:54 AM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.main;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import ss.com.bannerslider.ImageLoadingService;

public class GlideImageLoadingService implements ImageLoadingService {
  private Context context;

  public GlideImageLoadingService(Context context) {
    this.context = context;
  }

  @Override
  public void loadImage(String url, ImageView imageView) {
    Glide.with(context).load(url).into(imageView);
  }

  @Override
  public void loadImage(int resource, ImageView imageView) {
    Glide.with(context).load(resource).into(imageView);
  }

  @Override
  public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
    Glide.with(context).load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
  }
}
