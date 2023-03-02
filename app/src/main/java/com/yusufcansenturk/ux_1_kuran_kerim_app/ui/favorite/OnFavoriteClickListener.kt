package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite

import com.yusufcansenturk.ux_1_kuran_kerim_app.model.favorite.Favorite


interface OnFavoriteClickListener {
    fun onFavoriteClickListener(position: Int, favoriteList : ArrayList<Favorite>)
}