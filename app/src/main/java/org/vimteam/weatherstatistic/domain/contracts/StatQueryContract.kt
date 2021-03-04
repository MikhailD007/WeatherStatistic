package org.vimteam.weatherstatistic.domain.contracts

import androidx.lifecycle.LiveData
import org.vimteam.weatherstatistic.domain.models.StatQueryState

interface StatQueryContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val statQueryState: LiveData<StatQueryState>

        abstract fun getRequestsHistoryList()

    }
}