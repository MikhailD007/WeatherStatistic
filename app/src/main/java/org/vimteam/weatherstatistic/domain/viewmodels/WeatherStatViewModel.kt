package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import org.vimteam.weatherstatistic.domain.models.state.WeatherStatState

class WeatherStatViewModel(
    private val apiRepo: WeatherStatRepositoryContract,
    private val dbRepo: DatabaseRepositoryContract
) : WeatherStatContract.ViewModel() {

    override val weatherStatState = MutableLiveData<WeatherStatState>()

    override fun getWeatherData(place: String, dateFrom: LocalDate, dateTo: LocalDate) {
        weatherStatState.value = WeatherStatState.Loading
        viewModelScope.launch {
            apiRepo.getWeatherData(
                place, dateFrom, dateTo,
                {
                    weatherStatState.postValue(WeatherStatState.Error(it))
                },
                {
                    weatherStatState.postValue(WeatherStatState.Success(it))
                }
            )
        }
    }

    override fun proceedWeatherData(weatherStat: ArrayList<WeatherStat>) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.saveRequestHistory(weatherStat)
        }
        weatherStatState.postValue(WeatherStatState.Success(weatherStat))
    }


}