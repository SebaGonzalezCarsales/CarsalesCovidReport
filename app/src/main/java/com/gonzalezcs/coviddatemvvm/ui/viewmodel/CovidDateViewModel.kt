package com.gonzalezcs.coviddatemvvm.ui.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonzalezcs.coviddatemvvm.data.Repository.CovidDateRepository
import com.gonzalezcs.coviddatemvvm.data.model.CovidDateModel
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import com.gonzalezcs.coviddatemvvm.domain.GetCovidByDateUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CovidDateViewModel @Inject constructor(private val getCovidByDateUseCase: GetCovidByDateUseCase): ViewModel() {

    val covidDateLiveData: MutableLiveData<DataCovidModel?> = MutableLiveData()
    val appLoadingLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getCovidByDate(date: String){
        viewModelScope.launch {
            appLoadingLiveData.postValue(View.VISIBLE)
            val result = getCovidByDateUseCase.getCovidByDate(date)

            if(result!=null){
                covidDateLiveData.postValue(result)
                appLoadingLiveData.postValue(View.GONE)
            }
        }

    }
}