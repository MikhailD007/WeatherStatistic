package org.vimteam.weatherstatistic.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class WeatherStat(
    val date: Date,
    val city: City,
    val minTemperature: Int,
    val maxTemperature: Int,
    val windChillTemperature: Int,
    val heatIndexTemperature: Int,
    val cloudCover: Int,
    val precipitation: Float,
    val precipitationCover: Int,
    val relativeHumidity: Int,
    val visibilityRange: Int,
    val snow: Float,
    val snowDepth: Int,
    val windSpeed: Int,
    val windGust: Int,
    val weatherType: String,
    val conditions: String
): Parcelable
