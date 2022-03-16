package com.gonzalezcs.coviddatemvvm.data.network

import com.gonzalezcs.coviddatemvvm.data.model.CovidDateModel

interface CovidDateListener {

    interface CovidDateListener {
        fun onSuccess(covidModel: CovidDateModel?)
        fun onError(error: Throwable)
    }
}