package org.vimteam.weatherstatistic.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.LocalDate

@Parcelize
data class RequestHistory(
    val place: City,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val weatherDataList: ArrayList<WeatherStatistic>
): Parcelable
