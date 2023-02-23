package com.yusufcansenturk.ux_1_kuran_kerim_app.model.Notes

import com.google.firebase.firestore.FieldValue

data class Notes(
    val title : String?,
    val note :String?,
    val time : Any?,
    val email :String
)
