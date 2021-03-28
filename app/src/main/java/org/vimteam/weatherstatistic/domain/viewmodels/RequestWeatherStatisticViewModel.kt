package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.contracts.SharedPreferencesContract
import org.vimteam.weatherstatistic.domain.contracts.RequestWeatherStatisticContract
import org.vimteam.weatherstatistic.domain.models.state.RequestWeatherStatisticState

class RequestWeatherStatisticViewModel(
    private val dbRepo: DatabaseRepositoryContract,
    private val preferences: SharedPreferencesContract
) : RequestWeatherStatisticContract.ViewModel() {

    override val requestWeatherStatisticState = MutableLiveData<RequestWeatherStatisticState>()
    override val place = MutableLiveData<String>()
    override val dateFrom = MutableLiveData<LocalDate>()
    override val dateTo = MutableLiveData<LocalDate>()

    init {
        place.value = preferences.place
    }

    override fun getRequestsHistoryList() {
        requestWeatherStatisticState.value = RequestWeatherStatisticState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.getRequestsHistoryList(
                {
                    requestWeatherStatisticState.postValue(RequestWeatherStatisticState.Error(it))
                },
                {
                    requestWeatherStatisticState.postValue(RequestWeatherStatisticState.Success(it))
                }
            )
        }
    }

    override fun setPlace(place: String) {
        preferences.place = place
        this.place.value = place
    }

    override fun setDateFrom(date: LocalDate) {
        dateFrom.value = date
    }

    override fun setDateTo(date: LocalDate) {
        dateTo.value = date
    }

}