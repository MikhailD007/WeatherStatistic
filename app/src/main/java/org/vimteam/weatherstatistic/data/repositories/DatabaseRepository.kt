package org.vimteam.weatherstatistic.data.repositories

import org.vimteam.weatherstatistic.data.database.WeatherDB
import org.vimteam.weatherstatistic.data.mappers.RequestHistoryMapper
import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity
import org.vimteam.weatherstatistic.domain.contracts.DatabaseRepositoryContract
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic

class DatabaseRepository(
    private val db: WeatherDB
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
                    requestHistoryList.add(RequestHistoryMapper.requestHistoryEntityToRequestHistory(requestHistoryEntityListByPlace))
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

}