package org.vimteam.weatherstatistic.data.api.interceptors

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import org.vimteam.weatherstatistic.data.models.api.ApiError
import org.vimteam.weatherstatistic.data.models.api.ApiResponse
import org.vimteam.weatherstatistic.data.models.api.LocationDataResponse
import org.vimteam.weatherstatistic.data.models.api.WeatherDataResponse

class VisualcrossingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBodyString = response.body()?.string() ?: ""
        val emptyWeatherData = WeatherDataResponse(LocationDataResponse(ArrayList(), ""))
        when (response.code()) {
            200 -> {
                if (responseBodyString.contains("errorCode")) {
                    return response.newBuilder().body(
                        createResponseBody(
                            Gson().toJson(
                                ApiResponse(
                                    emptyWeatherData,
                                    ApiError(
                                        JSONObject(responseBodyString).getString("errorCode"),
                                        JSONObject(responseBodyString).getString("message")
                                    )
                                )
                            )
                        )
                    )
                        .code(200)
                        .build()
                }
            }
            else -> {
                return response.newBuilder().body(
                    createResponseBody(
                        Gson().toJson(
                            ApiResponse(
                                emptyWeatherData,
                                ApiError(
                                    response.code().toString(),
                                    responseBodyString.split("<title>")[1].split("</title>")[0]
                                )
                            )
                        )
                    )
                )
                    .code(200)
                    .build()
            }
        }
        val newResponseBody = if (responseBodyString.isEmpty()) {
            createResponseBody(
                Gson().toJson(
                    ApiResponse(
                        emptyWeatherData,
                        ApiError(
                            "999",
                            "Empty response"
                        )
                    )
                )
            )
        } else {
            createResponseBody(
                "{\"apiError\":" + Gson().toJson(ApiError()) +
                        ",\"weatherDataResponse\":" + responseBodyString +
                        "}"
            )
        }
        return response.newBuilder().body(newResponseBody).build()
    }

    private fun createResponseBody(json: String): ResponseBody = ResponseBody.create(
        MediaType.get("application/json; charset=utf-8"),
        json
    )

}