package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract
import org.vimteam.weatherstatistic.domain.contracts.StatQueryContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.StatQueryState
import java.lang.Exception

class StatQueryViewModel(
    private val repo: WeatherStatRepositoryContract,
    private val res: ResourcesProviderContract
) : StatQueryContract.ViewModel() {

    override val statQueryState = MutableLiveData<StatQueryState>()

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


}