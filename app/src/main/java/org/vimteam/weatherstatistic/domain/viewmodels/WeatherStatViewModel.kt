package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatContract
import org.vimteam.weatherstatistic.domain.models.WeatherStatState

class WeatherStatViewModel(
    res: ResourcesProviderContract
) : WeatherStatContract.ViewModel() {

    override val weatherStatState = MutableLiveData<WeatherStatState>()

}