package org.vimteam.weatherstatistic.domain.models

sealed class WeatherStatState{
    data class Success(val requestData: ArrayList<WeatherStat>) : WeatherStatState()
    data class Error(val error: Throwable) : WeatherStatState()
    object Loading : WeatherStatState()
}
