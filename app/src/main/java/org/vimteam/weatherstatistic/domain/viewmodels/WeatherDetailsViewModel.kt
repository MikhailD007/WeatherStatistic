package org.vimteam.weatherstatistic.domain.viewmodels

import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherDetailsContract

class WeatherDetailsViewModel(
    res: ResourcesProviderContract
) : WeatherDetailsContract.ViewModel() {
}