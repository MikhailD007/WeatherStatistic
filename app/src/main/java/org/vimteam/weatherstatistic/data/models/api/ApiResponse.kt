package org.vimteam.weatherstatistic.data.models.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("weatherDataResponse")
    val weatherDataResponse: WeatherDataResponse,
    @SerializedName("apiError")
    val apiError: ApiError
)