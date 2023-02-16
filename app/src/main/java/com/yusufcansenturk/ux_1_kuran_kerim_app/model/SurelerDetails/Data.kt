package com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("pageNumber")
    val pageNumber: Int?,

    @SerializedName("slug")
    val slug: String?,

    @SerializedName("verses")
    val verses: ArrayList<Verse>?

)