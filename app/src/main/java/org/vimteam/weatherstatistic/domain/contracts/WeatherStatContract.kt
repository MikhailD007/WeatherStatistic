package org.vimteam.weatherstatistic.domain.contracts

import androidx.lifecycle.LiveData
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import org.vimteam.weatherstatistic.domain.models.WeatherStatState

interface WeatherStatContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val weatherStatState: LiveData<WeatherStatState>

        abstract fun getWeatherData(place: String, dateFrom: LocalDate, dateTo: LocalDate)

        abstract fun proceedWeatherData(weatherStat: ArrayList<WeatherStat>)
    }
}