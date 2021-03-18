package org.vimteam.weatherstatistic.domain.contracts

import androidx.lifecycle.LiveData
import org.vimteam.weatherstatistic.domain.models.WeatherStatState

interface WeatherStatContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val weatherStatState: LiveData<WeatherStatState>

    }
}