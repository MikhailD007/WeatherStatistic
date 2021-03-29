package org.vimteam.weatherstatistic.data.mappers

import org.vimteam.weatherstatistic.data.models.LocationDataResponse
import org.vimteam.weatherstatistic.data.models.WeatherDataDayResponse
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.WeatherStat

object WeatherMapper {

    fun weatherDataDayResponseToWeatherStat(
        weatherDataDayResponse: WeatherDataDayResponse,
        city: City
    ): WeatherStat = weatherDataDayResponse.run {
        WeatherStat(
            maxTemperature = this.maxTemperature,
            minTemperature = this.minTemperature,
            city = city,
            date = this.date
        )
    }

    fun locationDataToWeatherStatList(locationDataResponse: LocationDataResponse): List<WeatherStat> =
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