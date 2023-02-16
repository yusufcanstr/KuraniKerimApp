package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.details

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.SureDetail
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.Verse
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.SureRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: SureRepository
): ViewModel(){

    val sureList = MutableLiveData<List<Verse>>()
    val errorMessage = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()


    fun getData(sure_id: String, context: Context) = viewModelScope.launch {
        isLoading.value = true
        val response = repository.getSureDetails(sure_id)
        when (response) {
            is Resource.Success -> {
                val sureListData = response.data!!.data.verses
                sureList.value = sureListData as List<Verse>?
                errorMessage.value = false
                isLoading.value = false
            }
            is Resource.Error -> {
                Toast.makeText(context, "Ayetler yükleniyor ...", Toast.LENGTH_LONG).show()
            }

            is Resource.Loading -> {
                Toast.makeText(context, "İnternet bağlantınızı kontrol ediniz !", Toast.LENGTH_LONG).show()
            }

            else -> {}
        }
    }



}