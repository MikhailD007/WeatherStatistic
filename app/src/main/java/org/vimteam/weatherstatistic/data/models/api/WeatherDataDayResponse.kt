package org.vimteam.weatherstatistic.data.models.api

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDate

data class WeatherDataDayResponse(
    @SerializedName("temp")
    val temperature: Float,
    @SerializedName("maxt")
    val maxTemperature: Float,
    @SerializedName("mint")
    val minTemperature: Float,
    @SerializedName("datetimeStr")
    val date: LocalDate
)
