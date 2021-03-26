package org.vimteam.weatherstatistic.data.models.api

import com.google.gson.annotations.SerializedName

data class WeatherDataResponse(
    @SerializedName("location")
    val location: LocationDataResponse
)
