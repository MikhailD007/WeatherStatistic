package org.vimteam.weatherstatistic.data.models.api

import com.google.gson.annotations.SerializedName

data class LocationDataResponse(
    @SerializedName("values")
    val weatherDataDayArray: List<WeatherDataDayResponse>,
    @SerializedName("address")
    val address: String
)
