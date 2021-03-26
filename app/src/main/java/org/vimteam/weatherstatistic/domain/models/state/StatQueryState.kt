package org.vimteam.weatherstatistic.domain.models.state

import org.vimteam.weatherstatistic.domain.models.RequestHistory

sealed class StatQueryState{
    data class Success(val requestsHistory: ArrayList<RequestHistory>) : StatQueryState()
    data class Error(val error: Throwable) : StatQueryState()
    object Loading : StatQueryState()
}
