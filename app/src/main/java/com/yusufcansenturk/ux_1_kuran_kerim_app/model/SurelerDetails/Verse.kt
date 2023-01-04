package com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails


import com.google.gson.annotations.SerializedName

data class Verse(
    val id: Int?,
    val juzNumber: Int?,
    val page: Int?,
    @SerializedName("surah_id")
    val surahİd: Int?,
    val transcription: String?,
    val translation: Translation?,
    val verse: String?,
    @SerializedName("verse_number")
    val verseNumber: Int?
)