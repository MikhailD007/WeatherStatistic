package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.contracts.ApiRepositoryContract
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.contracts.ResultWeatherStatisticContract
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic
import org.vimteam.weatherstatistic.domain.models.state.ResultWeatherStatisticState

class ResultWeatherStatisticViewModel(
    private val apiRepo: ApiRepositoryContract,
    private val dbRepo: DatabaseRepositoryContract
) : ResultWeatherStatisticContract.ViewModel() {

    override val resultWeatherStatisticState = MutableLiveData<ResultWeatherStatisticState>()

    override fun getWeatherData(place: String, dateFrom: LocalDate, dateTo: LocalDate) {
        resultWeatherStatisticState.value = ResultWeatherStatisticState.Loading
        viewModelScope.launch {
            apiRepo.getWeatherData(
                place, dateFrom, dateTo,
                {
                    resultWeatherStatisticState.postValue(ResultWeatherStatisticState.Error(it))
                },
                {
                    resultWeatherStatisticState.postValue(ResultWeatherStatisticState.Success(it))
                }
            )
        }
    }

    override fun proceedWeatherData(weatherStatistic: ArrayList<WeatherStatistic>) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.saveRequestHistory(weatherStatistic)
        }
        resultWeatherStatisticState.postValue(ResultWeatherStatisticState.Success(weatherStatistic))
    }


}