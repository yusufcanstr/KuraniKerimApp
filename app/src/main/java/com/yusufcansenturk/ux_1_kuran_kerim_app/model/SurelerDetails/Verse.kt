package com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails


import com.google.gson.annotations.SerializedName

data class Verse(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("surah_id")
    val surahİd: Int?,

    @SerializedName("juzNumber")
    val juzNumber: Int?,

    @SerializedName("")
    val page: Int?,

    @SerializedName("transcription")
    val transcription: String?,

    @SerializedName("translation")
    val translation: Translation?,

    @SerializedName("verse")
    val verse: String?,

    @SerializedName("verse_number")
    val verseNumber: Int?
)