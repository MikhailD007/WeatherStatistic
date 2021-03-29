package org.vimteam.weatherstatistic.domain.models.state

import org.vimteam.weatherstatistic.domain.models.Contact

sealed class ContactsListState {
    data class Success(val contacts: ArrayList<Contact>) : ContactsListState()
    data class Error(val error: Throwable) : ContactsListState()
    object Loading : ContactsListState()
}
