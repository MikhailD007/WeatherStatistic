package org.vimteam.weatherstatistic.domain.viewmodels

import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract
import org.vimteam.weatherstatistic.domain.contracts.StatQueryContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract

class StatQueryViewModel(
    private val repo: WeatherStatRepositoryContract,
    private val res: ResourcesProviderContract
): StatQueryContract.ViewModel() {


}