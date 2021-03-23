package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract
import org.vimteam.weatherstatistic.domain.contracts.StatQueryContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.StatQueryState
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import java.lang.Exception

class StatQueryViewModel(
    private val repo: WeatherStatRepositoryContract,
    private val res: ResourcesProviderContract
) : StatQueryContract.ViewModel() {

    override val statQueryState = MutableLiveData<StatQueryState>()
    override val dateFrom = MutableLiveData<LocalDate>()
    override val dateTo = MutableLiveData<LocalDate>()

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

    override fun setDateFrom(date: LocalDate) {
        dateFrom.value = date
    }

    override fun setDateTo(date: LocalDate) {
        dateTo.value = date
    }

}