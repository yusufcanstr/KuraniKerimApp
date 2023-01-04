package com.yusufcansenturk.ux_1_kuran_kerim_app.service

import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.SureDetail
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.SureNameList
import retrofit2.http.GET
import retrofit2.http.Path


interface ServiceAPI {

    // BU HATAYI ÇÖZ !!!!
    //Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 10 path $.data


    @GET("surahs")
    suspend fun getSurelerList() : SureNameList

    @GET("surah/{id}")
    suspend fun getSurelerDetails(@Path("id") id: String) : SureDetail


}