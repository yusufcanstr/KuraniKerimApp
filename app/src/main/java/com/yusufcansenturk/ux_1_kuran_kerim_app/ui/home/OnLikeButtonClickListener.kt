package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import android.widget.ImageView
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data

interface OnLikeButtonClickListener {
    fun onLikeButtonClicked(position: Int, sureList: List<Data>, img1: ImageView, img2: ImageView)
}