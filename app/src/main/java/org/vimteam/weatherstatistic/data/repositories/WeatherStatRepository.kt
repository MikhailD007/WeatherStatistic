package org.vimteam.weatherstatistic.data.repositories

import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import java.lang.Thread.sleep
import java.sql.Date

class WeatherStatRepository(
    api: ApiContract,
    db: DatabaseContract
) : WeatherStatRepositoryContract {

    override fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit) {
        Thread {
            sleep(3000)
            val dumbCity1 = City("123", "Камышин", 51.1354, 46.358)
            val weatherStatList1 = ArrayList<WeatherStat>()
            weatherStatList1.add(
                WeatherStat(
                    date = Date(1262304000000),
                    city = dumbCity1,
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
            weatherStatList1.add(
                WeatherStat(
                    date = Date(1614816000000),
                    city = dumbCity1,
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
            val dumbRequestHistory1 = RequestHistory(
                city = dumbCity1,
                dateFrom = Date(1262304000000),
                dateTo = Date(1614816000000),
                weatherStatList1
            )
            //--------------------------------------------------------------
            val dumbCity2 = City("34234", "Тольятти", 50.6786, 45.5675)
            val weatherStatList2 = ArrayList<WeatherStat>()
            weatherStatList2.add(
                WeatherStat(
                    date = Date(1262304000000),
                    city = dumbCity1,
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
            weatherStatList1.add(
                WeatherStat(
                    date = Date(1614816000000),
                    city = dumbCity1,
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
            val dumbRequestHistory2 = RequestHistory(
                city = dumbCity2,
                dateFrom = Date(1262304000000),
                dateTo = Date(1614816000000),
                weatherStatList2
            )
            //--------------------------------------------------------------

            val resultList = ArrayList<RequestHistory>()
            resultList.add(dumbRequestHistory1)
            resultList.add(dumbRequestHistory2)
            func.invoke(resultList)
        }.start()
    }

}