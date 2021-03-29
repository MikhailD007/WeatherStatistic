package org.vimteam.weatherstatistic.data.repositories

import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.App
import org.vimteam.weatherstatistic.data.api.interfaces.VisualcrossingInterface
import org.vimteam.weatherstatistic.data.mappers.WeatherMapper
import org.vimteam.weatherstatistic.domain.contracts.ApiRepositoryContract
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic
import java.lang.Thread.sleep

class ApiRepository(
    private val visualcrossingApi: VisualcrossingInterface
) : ApiRepositoryContract {

    override suspend fun getWeatherData(
        place: String,
        dateFrom: LocalDate,
        dateTo: LocalDate,
        errorFunc: (Exception) -> Unit,
        successFunc: (ArrayList<WeatherStatistic>) -> Unit
    ) {
        val weatherStatistic: ArrayList<WeatherStatistic> = ArrayList()
        for (i in App.HISTORY_YEARS - 1 downTo 0) {
            val response = visualcrossingApi.getWeatherData(
                locations = place,
                startDateTime = dateFrom.minusYears(i).toString(),
                endDateTime = dateTo.minusYears(i).toString()
            )
            if (response.apiError.errorCode.isNotEmpty()) {
                errorFunc.invoke(Exception(response.apiError.errorDescription))
                return
            } else {
                weatherStatistic.addAll(
                    WeatherMapper.locationDataToWeatherStatList(
                        response.weatherDataResponse.location
                    ) as ArrayList<WeatherStatistic>
                )
            }
            sleep(100)
        }
        successFunc.invoke(weatherStatistic)
    }

}