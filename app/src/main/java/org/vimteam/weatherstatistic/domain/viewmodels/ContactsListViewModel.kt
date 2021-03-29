package org.vimteam.weatherstatistic.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vimteam.weatherstatistic.domain.contracts.ContactsListContract
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.models.state.ContactsListState

class ContactsListViewModel(
    private val dbRepo: DatabaseRepositoryContract
) : ContactsListContract.ViewModel() {

    override val contactsListState = MutableLiveData<ContactsListState>()

    override fun getContactsList() {
        contactsListState.value = ContactsListState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.getContactsList(
                {
                    contactsListState.postValue(ContactsListState.Error(it))
                },
                {
                    contactsListState.postValue(ContactsListState.Success(it))
                }
            )
        }
    }

}