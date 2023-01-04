package com.yusufcansenturk.ux_1_kuran_kerim_app.repository

import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.SureDetail
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.SureNameList
import com.yusufcansenturk.ux_1_kuran_kerim_app.service.ServiceAPI
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class SureRepository @Inject constructor(
    private val api: ServiceAPI
){


    suspend fun getSureList() : Resource<SureNameList> {
        val response = try {
            api.getSurelerList()
        }catch (e:Exception) {
            println(e.toString())
            return Resource.Error("Error!")
        }

        return Resource.Success(response)
    }

    suspend fun getSureDetails(id : String) : Resource<SureDetail> {
        val response = try {
            api.getSurelerDetails(id)
        }catch (e:Exception) {
            println("SureRepository Error2")
            return  Resource.Error("Error!")
        }

        return Resource.Success(response)

    }


}