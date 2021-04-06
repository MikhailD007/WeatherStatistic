package org.vimteam.weatherstatistic.domain.models.state

import org.vimteam.weatherstatistic.domain.models.RequestHistory

sealed class RequestWeatherStatisticState{
    data class Success(val requestsHistory: ArrayList<RequestHistory>) : RequestWeatherStatisticState()
    data class Error(val error: Throwable) : RequestWeatherStatisticState()
    object Loading : RequestWeatherStatisticState()
}
