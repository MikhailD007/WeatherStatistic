package org.vimteam.weatherstatistic.data.repositories

import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.App
import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.data.mappers.WeatherMapper
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStat
import java.lang.Thread.sleep
import java.sql.Date

class WeatherStatRepository(
    val api: ApiContract,
    val db: DatabaseContract
) : WeatherStatRepositoryContract {

    override fun getRequestsHistoryList(func: (ArrayList<RequestHistory>) -> Unit) {
        Thread {
            sleep(100)
            val dumbCity1 = City("123", "Камышин", 51.1354, 46.358)
            val weatherStatList1 = ArrayList<WeatherStat>()
            weatherStatList1.add(
                WeatherStat(
                    date = LocalDate(1262304000000),
                    city = dumbCity1,
                    minTemperature = -5.0F,
                    maxTemperature = 10.0F,
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
                    date = LocalDate(1614816000000),
                    city = dumbCity1,
                    minTemperature = -3.0F,
                    maxTemperature = 0.0F,
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
                    date = LocalDate(1262304000000),
                    city = dumbCity1,
                    minTemperature = -5.0F,
                    maxTemperature = 10.0F,
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
                    date = LocalDate(1614816000000),
                    city = dumbCity1,
                    minTemperature = -3.0F,
                    maxTemperature = 0.0F,
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

    override suspend fun getWeatherData(
        place: String,
        dateFrom: LocalDate,
        dateTo: LocalDate,
        func: (ArrayList<WeatherStat>) -> Unit
    ) {
        val weatherStat: ArrayList<WeatherStat> = ArrayList()
        for (i in App.HISTORY_YEARS - 1 downTo 0) {
            weatherStat.addAll(
                WeatherMapper.locationDataToWeatherStatList(
                    api.getWeatherData(
                        locations = place,
                        startDateTime = dateFrom.minusYears(i).toString(),
                        endDateTime = dateTo.minusYears(i).toString()
                    ).location
                ) as ArrayList<WeatherStat>
            )
            sleep(100)
        }
        func.invoke(weatherStat)
    }

}