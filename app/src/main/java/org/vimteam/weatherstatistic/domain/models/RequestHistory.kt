package org.vimteam.weatherstatistic.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
data class RequestHistory(
    val city: City,
    val dateFrom: Date,
    val dateTo: Date,
    val weatherDataList: ArrayList<WeatherStat>
): Parcelable
