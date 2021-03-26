package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.contracts.SharedPreferencesContract
import org.vimteam.weatherstatistic.domain.contracts.StatQueryContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.state.StatQueryState

class StatQueryViewModel(
    private val repo: WeatherStatRepositoryContract,
    private val preferences: SharedPreferencesContract
) : StatQueryContract.ViewModel() {

    override val statQueryState = MutableLiveData<StatQueryState>()
    override val place = MutableLiveData<String>()
    override val dateFrom = MutableLiveData<LocalDate>()
    override val dateTo = MutableLiveData<LocalDate>()

    init {
        place.value = preferences.place
    }

    override fun getRequestsHistoryList() {
        statQueryState.value = StatQueryState.Loading
        try {
            repo.getRequestsHistoryList {
                statQueryState.postValue(StatQueryState.Success(it))
            }
        } catch (e: Exception) {
            statQueryState.postValue(StatQueryState.Error(e))
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