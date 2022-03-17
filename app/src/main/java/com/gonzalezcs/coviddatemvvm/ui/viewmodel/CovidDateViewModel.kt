package com.gonzalezcs.coviddatemvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import com.gonzalezcs.coviddatemvvm.domain.GetCovidByDateUseCase
import com.gonzalezcs.coviddatemvvm.ui.utils.StateView
import kotlinx.coroutines.launch
import javax.inject.Inject

class CovidDateViewModel @Inject constructor(private val getCovidByDateUseCase: GetCovidByDateUseCase): ViewModel() {
    val covidDateLiveData: MutableLiveData<DataCovidModel?> = MutableLiveData()
    val appLoadingLiveData: MutableLiveData<Int> = MutableLiveData()
    val covidStateViewLiveData: MutableLiveData<StateView<DataCovidModel>> = MutableLiveData()
    fun getCovidByDate(date: String){
        viewModelScope.launch {
            covidStateViewLiveData.postValue(StateView.loading())
            val result = getCovidByDateUseCase.getCovidByDate(date)
            if(result!=null){
                covidStateViewLiveData.postValue(StateView.success(result))
            }else{
                covidStateViewLiveData.postValue(StateView.error("LabraChupalo"))
            }
        }
    }
}