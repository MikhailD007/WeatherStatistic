package org.vimteam.weatherstatistic.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.LocalDate
import java.util.*

@Parcelize
data class WeatherStatistic(
    val date: LocalDate,
    val place: City,
    val minTemperature: Float,
    val maxTemperature: Float,
    val windChillTemperature: Int = 0,
    val heatIndexTemperature: Int = 0,
    val cloudCover: Int = 0,
    val precipitation: Float = 0.0F,
    val precipitationCover: Int = 0,
    val relativeHumidity: Int = 0,
    val visibilityRange: Int = 0,
    val snow: Float = 0.0F,
    val snowDepth: Int = 0,
    val windSpeed: Int= 0,
    val windGust: Int= 0,
    val weatherType: String = "",
    val conditions: String = ""
): Parcelable
