package org.vimteam.weatherstatistic.domain.contracts

import androidx.lifecycle.LiveData
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.domain.models.state.ContactsListState
import org.vimteam.weatherstatistic.domain.models.state.RequestWeatherStatisticState

interface ContactsListContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val contactsListState: LiveData<ContactsListState>

        abstract fun getContactsList()

    }
}