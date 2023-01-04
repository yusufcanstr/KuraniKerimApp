package com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails

import com.google.gson.annotations.SerializedName

data class Data(
    val id: Int?,
    val name: String?,
    val pageNumber: Int?,
    val slug: String?,
    @SerializedName("verse_count")
    val verseCount: Int?,
    val verses: List<Verse?>?,
    val zero: Any?
)