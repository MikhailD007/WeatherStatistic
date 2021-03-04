package org.vimteam.weatherstatistic.domain.models

import java.sql.Date

data class RequestHistory(
    val city: City,
    val dateFrom: Date,
    val dateTo: Date,
    val weatherDataList: ArrayList<WeatherStat>
)
