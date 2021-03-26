package org.vimteam.weatherstatistic.domain.contracts

import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat

interface DatabaseRepositoryContract {

    @Throws(Exception::class)
    suspend fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit)

    @Throws(Exception::class)
    suspend fun saveRequestHistory(requestHistory: RequestHistory)

    @Throws(Exception::class)
    suspend fun saveRequestHistory(weatherStatList: ArrayList<WeatherStat>)

}