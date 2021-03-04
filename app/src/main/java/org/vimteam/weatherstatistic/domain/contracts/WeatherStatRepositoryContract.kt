package org.vimteam.weatherstatistic.domain.contracts

import org.vimteam.weatherstatistic.domain.models.RequestHistory

interface WeatherStatRepositoryContract {

    @Throws(Exception::class)
    fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit)

}