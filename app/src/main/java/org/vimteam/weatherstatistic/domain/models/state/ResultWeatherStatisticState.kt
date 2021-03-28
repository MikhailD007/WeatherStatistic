package org.vimteam.weatherstatistic.domain.models.state

import org.vimteam.weatherstatistic.domain.models.WeatherStatistic

sealed class ResultWeatherStatisticState{
    data class Success(val requestData: ArrayList<WeatherStatistic>) : ResultWeatherStatisticState()
    data class Error(val error: Throwable) : ResultWeatherStatisticState()
    object Loading : ResultWeatherStatisticState()
}
