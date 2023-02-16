package com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList


import com.google.gson.annotations.SerializedName
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.Verse

data class Data(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("name_original")
    val nameOriginal: String?,

    @SerializedName("pageNumber")
    val pageNumber: Int?,

    @SerializedName("slug")
    val slug: String?,

    @SerializedName("verse_count")
    val verseCount: Int?
)