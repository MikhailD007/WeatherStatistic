package org.vimteam.weatherstatistic.domain.contracts

import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic

interface DatabaseRepositoryContract {

    @Throws(Exception::class)
    suspend fun getRequestsHistoryList(
        error: (Exception) -> Unit,
        success: (ArrayList<RequestHistory>) -> Unit
    )

    @Throws(Exception::class)
    suspend fun saveRequestHistory(requestHistory: RequestHistory)

    @Throws(Exception::class)
    suspend fun saveRequestHistory(weatherStatisticList: ArrayList<WeatherStatistic>)

}