package org.vimteam.weatherstatistic.domain.contracts

import androidx.lifecycle.LiveData
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.state.StatQueryState

interface StatQueryContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val statQueryState: LiveData<StatQueryState>

        abstract val place: LiveData<String>
        abstract val dateFrom: LiveData<LocalDate>
        abstract val dateTo: LiveData<LocalDate>

        abstract fun getRequestsHistoryList()

        abstract fun setPlace(place: String)

        abstract fun setDateFrom(date: LocalDate)

        abstract fun setDateTo(date: LocalDate)

    }
}