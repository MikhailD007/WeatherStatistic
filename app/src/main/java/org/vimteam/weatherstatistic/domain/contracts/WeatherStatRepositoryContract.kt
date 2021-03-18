package org.vimteam.weatherstatistic.domain.contracts

import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat

interface WeatherStatRepositoryContract {

    @Throws(Exception::class)
    fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit)

    @Throws(Exception::class)
    suspend fun getWeatherData(place: String, dateFrom: LocalDate, dateTo: LocalDate, func: (ArrayList<WeatherStat>) -> Unit)

}