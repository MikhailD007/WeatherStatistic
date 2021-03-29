package org.vimteam.weatherstatistic.data.repositories

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import org.vimteam.weatherstatistic.data.database.WeatherDB
import org.vimteam.weatherstatistic.data.mappers.RequestHistoryMapper
import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.models.Contact
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic


class DatabaseRepository(
    private val db: WeatherDB,
    private val contentResolver: ContentResolver
) : DatabaseRepositoryContract {
    override suspend fun getRequestsHistoryList(
        error: (Exception) -> Unit,
        success: (ArrayList<RequestHistory>) -> Unit
    ) {
        try {
            val requestHistoryEntityList = ArrayList(db.requestHistoryDao().allRecords())
            val requestHistoryEntityListByPlace = ArrayList<RequestHistoryEntity>()
            val requestHistoryList = ArrayList<RequestHistory>()
            var place = ""
            for (requestHistoryEntity in requestHistoryEntityList) {
                if (requestHistoryEntity.place != place && place.isNotEmpty()) {
                    requestHistoryList.add(
                        RequestHistoryMapper.requestHistoryEntityToRequestHistory(
                            requestHistoryEntityListByPlace
                        )
                    )
                    requestHistoryEntityListByPlace.clear()
                }
                requestHistoryEntityListByPlace.add(requestHistoryEntity)
                place = requestHistoryEntity.place
            }
            requestHistoryList.add(RequestHistoryMapper.requestHistoryEntityToRequestHistory(requestHistoryEntityListByPlace))
            success.invoke(requestHistoryList)
        } catch (e: Exception) {
            error.invoke(e)
        }
    }

    override suspend fun saveRequestHistory(requestHistory: RequestHistory) {
        val requestHistoryEntityList = RequestHistoryMapper.requestHistoryToRequestHistoryEntity(requestHistory)
        for (record in requestHistoryEntityList) {
            db.requestHistoryDao().insert(record)
        }
    }

    override suspend fun saveRequestHistory(weatherStatisticList: ArrayList<WeatherStatistic>) {
        for (weatherStat in weatherStatisticList) {
            db.requestHistoryDao().insert(RequestHistoryMapper.weatherStatToRequestHistoryEntity(weatherStat))
        }
    }

    override suspend fun getContactsList(
        error: (Exception) -> Unit,
        success: (ArrayList<Contact>) -> Unit
    ) {
        try {
            val contactsList = ArrayList<Contact>()
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        var phone: String? = null

                        val phones: Cursor? = contentResolver.query(
                            Phone.CONTENT_URI,
                            null,
                            Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null
                        )
                        phones?.let {
                            phone =if (phones.moveToNext()) phones.getString(phones.getColumnIndex(Phone.NUMBER)) else null
                            phones.close()
                        }
                        contactsList.add(Contact(name, phone ?:""))
                    }
                }
                success.invoke(contactsList)
            }
            cursorWithContacts?.close()
        } catch (e: Exception) {
            error.invoke(e)
        }
    }

}