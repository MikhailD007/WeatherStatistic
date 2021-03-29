package org.vimteam.weatherstatistic.domain.contracts

import androidx.lifecycle.LiveData
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic
import org.vimteam.weatherstatistic.domain.models.state.ResultWeatherStatisticState

interface ResultWeatherStatisticContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val resultWeatherStatisticState: LiveData<ResultWeatherStatisticState>

        abstract fun getWeatherData(place: String, dateFrom: LocalDate, dateTo: LocalDate)

        abstract fun proceedWeatherData(weatherStatistic: ArrayList<WeatherStatistic>)
    }
}