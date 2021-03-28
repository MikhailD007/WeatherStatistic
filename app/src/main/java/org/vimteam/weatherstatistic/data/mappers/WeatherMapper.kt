package org.vimteam.weatherstatistic.data.mappers

import org.vimteam.weatherstatistic.data.models.api.LocationDataResponse
import org.vimteam.weatherstatistic.data.models.api.WeatherDataDayResponse
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic

object WeatherMapper {

    fun weatherDataDayResponseToWeatherStat(
        weatherDataDayResponse: WeatherDataDayResponse,
        place: City
    ): WeatherStatistic = weatherDataDayResponse.run {
        WeatherStatistic(
            maxTemperature = this.maxTemperature,
            minTemperature = this.minTemperature,
            place = place,
            date = this.date
        )
    }

    fun locationDataToWeatherStatList(locationDataResponse: LocationDataResponse): List<WeatherStatistic> =
        locationDataResponse.run {
            this.weatherDataDayArray.map {
                weatherDataDayResponseToWeatherStat(
                    it,
                    City(
                        googleUID = "",
                        name = this.address,
                        lat = 0.0,
                        lon = 0.0
                    )
                )
            }
        }

}