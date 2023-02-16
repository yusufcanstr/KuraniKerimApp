package com.yusufcansenturk.ux_1_kuran_kerim_app.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yusufcansenturk.ux_1_kuran_kerim_app.R

fun providesDownloadImage(context: Context, imageView: ImageView, url:String) {
    Glide.with(context)
        .load(url)
        .error(R.drawable.ic_launcher_background)
        .centerCrop()
        .into(imageView)
}

fun ImageView.downloadFromUrl(url:String?) {
    val options = RequestOptions()
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}