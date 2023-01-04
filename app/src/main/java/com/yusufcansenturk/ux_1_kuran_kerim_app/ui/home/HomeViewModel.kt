package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.SureRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SureRepository
) : ViewModel(){

    var sureList = MutableLiveData<List<Data>>() // ANA KOD
    var errorMessage = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()

    private var initialSureList = listOf<Data>()
    private var isSearchStarting = true


    init {
        loadSure()
    }


    fun searchSureList(query: String) {
        val listToSearch = if (isSearchStarting) {
            sureList.value
        }else {
            initialSureList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()){
                sureList.value = initialSureList
                isSearchStarting = true
                return@launch
            }

            val results = listToSearch!!.filter {
                it.name!!.contains(query.trim(), ignoreCase = true)
            }

            if (isSearchStarting) {
                initialSureList = sureList.value!!
                isSearchStarting = false
            }

            sureList.value = results

        }

    }


    fun loadSure() {
        viewModelScope.launch {

            isLoading.value = true

            when(val result = repository.getSureList()) {
                is Resource.Success -> {

                    val sureItems = result.data!!.data!!.mapIndexed { index, data ->
                        Data(
                            data?.id,
                            data?.name,
                            data?.nameOriginal,
                            data?.pageNumber,
                            data?.slug,
                            data?.verseCount
                        )

                        //println(data?.name)

                    } as List<Data>


                    println("Success Resource")
                    isLoading.value = false
                    errorMessage.value = false
                    //sureList.value?.plus(sureItems)
                    sureList.value = sureItems



                }

                is Resource.Error -> {
                    errorMessage.value = true
                    isLoading.value = false
                    println("Error Resource")
                }


                else -> {}
            }

        }

    }



}