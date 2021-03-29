package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.koin.experimental.property.inject
import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import org.vimteam.weatherstatistic.domain.models.WeatherStatState
import org.vimteam.weatherstatistic.ui.NetworkService

class WeatherStatViewModel(
    private val repo: WeatherStatRepositoryContract,
    res: ResourcesProviderContract
) : WeatherStatContract.ViewModel() {

    override val weatherStatState = MutableLiveData<WeatherStatState>()

    override fun getWeatherData(place: String, dateFrom: LocalDate, dateTo: LocalDate) {
        weatherStatState.value = WeatherStatState.Loading
        viewModelScope.launch {
            repo.getWeatherData(
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
        weatherStatState.postValue(WeatherStatState.Success(weatherStat))
    }


}