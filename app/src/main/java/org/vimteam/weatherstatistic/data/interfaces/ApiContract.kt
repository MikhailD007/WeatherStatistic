package org.vimteam.weatherstatistic.data.interfaces

import org.vimteam.weatherstatistic.data.models.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiContract {

    @GET("/VisualCrossingWebServices/rest/services/weatherdata/history")
    suspend fun getWeatherData(
        @Query("aggregateHours") aggregateHours: Int = 24,
        @Query("combinationMethod") combinationMethod: String = "aggregate",
        @Query("startDateTime") startDateTime: String,
        @Query("endDateTime") endDateTime: String,
        @Query("maxStations") maxStations: Int = -1,
        @Query("maxDistance") maxDistance: Int = -1,
        @Query("contentType") contentType: String = "json",
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("locationMode") locationMode: String = "single",
        @Query("key") key: String = "MU1Q3QAYQLGX4RJ4DRVQQU4WL",
        @Query("dataElements") dataElements: String = "default",
        @Query("locations") locations: String,
    ): WeatherDataResponse

}