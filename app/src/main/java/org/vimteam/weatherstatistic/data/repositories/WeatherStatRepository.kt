package org.vimteam.weatherstatistic.data.repositories

import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import java.lang.Thread.sleep
import java.sql.Date
import java.sql.SQLData

class WeatherStatRepository(
    api: ApiContract,
    db: DatabaseContract
) : WeatherStatRepositoryContract {

    override fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit) {
        Thread {
            sleep(5000)
            val dumbCity = City("123", "Камышин", 51.1354, 46.358)
            val weatherStatList = ArrayList<WeatherStat>()
            weatherStatList.add(
                WeatherStat(
                    date = Date(1612132112),
                    city = dumbCity,
                    minTemperature = -5,
                    maxTemperature = 10,
                    windChillTemperature = -8,
                    heatIndexTemperature = 0,
                    cloudCover = 65,
                    precipitation = 10.2F,
                    precipitationCover = 15,
                    relativeHumidity = 87,
                    visibilityRange = 9,
                    snow = 0F,
                    snowDepth = 0,
                    windSpeed = 4,
                    windGust = 10,
                    weatherType = "cloudy",
                    conditions = "local rains"
                )
            )
            weatherStatList.add(
                WeatherStat(
                    date = Date(1612192112),
                    city = dumbCity,
                    minTemperature = -3,
                    maxTemperature = 0,
                    windChillTemperature = -5,
                    heatIndexTemperature = 0,
                    cloudCover = 30,
                    precipitation = 0.0F,
                    precipitationCover = 0,
                    relativeHumidity = 50,
                    visibilityRange = 12,
                    snow = 0F,
                    snowDepth = 0,
                    windSpeed = 0,
                    windGust = 0,
                    weatherType = "clear",
                    conditions = ""
                )
            )
            val dumbRequestHistory = RequestHistory(
                city = dumbCity,
                dateFrom = Date(1612132112),
                dateTo = Date(1614867112),
                weatherStatList
                )
            val resultList = ArrayList<RequestHistory>()
            resultList.add(dumbRequestHistory)
            func.invoke(resultList)
        }.start()
    }

}