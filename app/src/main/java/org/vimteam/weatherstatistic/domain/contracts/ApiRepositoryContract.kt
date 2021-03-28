package org.vimteam.weatherstatistic.domain.contracts

import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic

interface ApiRepositoryContract {

    @Throws(Exception::class)
    suspend fun getWeatherData(
        place: String,
        dateFrom: LocalDate,
        dateTo: LocalDate,
        errorFunc: (java.lang.Exception) -> Unit,
        successFunc: (ArrayList<WeatherStatistic>) -> Unit
    )

}