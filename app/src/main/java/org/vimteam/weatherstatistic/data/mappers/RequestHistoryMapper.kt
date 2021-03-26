package org.vimteam.weatherstatistic.data.mappers

import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat

object RequestHistoryMapper {

    fun requestHistoryToRequestHistoryEntity(requestHistory: RequestHistory): ArrayList<RequestHistoryEntity> {
        val requestHistoryEntityList = ArrayList<RequestHistoryEntity>()
        val place = requestHistory.city.name
        for (record in requestHistory.weatherDataList) {
            requestHistoryEntityList.add(
                RequestHistoryEntity(
                    place,
                    record.date.toString(),
                    record.minTemperature,
                    record.maxTemperature
                )
            )
        }
        return requestHistoryEntityList
    }

    fun weatherStatToRequestHistoryEntity(weatherStat: WeatherStat): RequestHistoryEntity {
        return RequestHistoryEntity(
            weatherStat.city.name,
            weatherStat.date.toString(),
            weatherStat.minTemperature,
            weatherStat.maxTemperature
        )
    }

}