package org.vimteam.weatherstatistic.data.repositories

import org.vimteam.weatherstatistic.data.database.WeatherDB
import org.vimteam.weatherstatistic.data.mappers.RequestHistoryMapper
import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat

class DatabaseRepository(
    private val db: WeatherDB
) : DatabaseRepositoryContract {

    override suspend fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit) {

    }

    override suspend fun saveRequestHistory(requestHistory: RequestHistory) {
        val requestHistoryEntityList = RequestHistoryMapper.requestHistoryToRequestHistoryEntity(requestHistory)
        for (record in requestHistoryEntityList) {
            db.requestHistoryDao().insert(record)
        }
    }

    override suspend fun saveRequestHistory(weatherStatList: ArrayList<WeatherStat>) {
        for (weatherStat in weatherStatList) {
            db.requestHistoryDao().insert(RequestHistoryMapper.weatherStatToRequestHistoryEntity(weatherStat))
        }
    }
}