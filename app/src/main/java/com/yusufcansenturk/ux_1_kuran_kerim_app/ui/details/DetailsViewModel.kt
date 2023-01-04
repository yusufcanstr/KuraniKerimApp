package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.details

import androidx.lifecycle.ViewModel
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.SureDetail
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.SureRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: SureRepository
): ViewModel(){

    suspend fun getSure(id:String) : Resource<SureDetail> {
        return repository.getSureDetails(id)
    }


}