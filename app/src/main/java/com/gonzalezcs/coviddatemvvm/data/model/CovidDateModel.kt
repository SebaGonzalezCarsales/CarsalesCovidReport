package com.gonzalezcs.coviddatemvvm.data.model

import com.google.gson.annotations.SerializedName

data class CovidDateModel (
    @SerializedName("data") val data : DataCovidModel
)