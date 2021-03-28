package org.vimteam.weatherstatistic.data.mappers

import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic

object RequestHistoryMapper {

    fun requestHistoryToRequestHistoryEntity(requestHistory: RequestHistory): ArrayList<RequestHistoryEntity> {
        val requestHistoryEntityList = ArrayList<RequestHistoryEntity>()
        val place = requestHistory.place.name
        for (record in requestHistory.weatherDataList) {
            requestHistoryEntityList.add(
                RequestHistoryEntity(
                    place,
                    record.date,
                    record.minTemperature,
                    record.maxTemperature
                )
            )
        }
        return requestHistoryEntityList
    }

    fun weatherStatToRequestHistoryEntity(weatherStatistic: WeatherStatistic): RequestHistoryEntity {
        return RequestHistoryEntity(
            weatherStatistic.place.name,
            weatherStatistic.date,
            weatherStatistic.minTemperature,
            weatherStatistic.maxTemperature
        )
    }

    fun requestHistoryEntityToRequestHistory(requestHistoryEntityList: ArrayList<RequestHistoryEntity>): RequestHistory {
        val place = City(
            "",
            name = requestHistoryEntityList[0].place,
            0.0,
            0.0
        )
        val fromDate = requestHistoryEntityList[0].date
        val toDate = requestHistoryEntityList[requestHistoryEntityList.size - 1].date
        val requestHistory = RequestHistory(
            place,
            fromDate,
            toDate,
            ArrayList()
        )
        for (requestHistoryEntity in requestHistoryEntityList) {
            requestHistory.weatherDataList.add(
                WeatherStatistic(
                    requestHistoryEntity.date,
                    place,
                    minTemperature = requestHistoryEntity.minTemperature,
                    maxTemperature = requestHistoryEntity.maxTemperature
                )
            )
        }
        return requestHistory
    }

}