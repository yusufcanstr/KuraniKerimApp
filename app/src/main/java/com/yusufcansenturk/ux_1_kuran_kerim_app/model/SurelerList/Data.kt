package com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList


import com.google.gson.annotations.SerializedName

data class Data(
    val id: Int?,
    val name: String?,
    @SerializedName("name_original")
    val nameOriginal: String?,
    val pageNumber: Int?,
    val slug: String?,
    @SerializedName("verse_count")
    val verseCount: Int?
)