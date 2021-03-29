package org.vimteam.weatherstatistic.data.api.interceptors

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import org.vimteam.weatherstatistic.base.parseFromJson
import org.vimteam.weatherstatistic.data.models.ApiError
import org.vimteam.weatherstatistic.data.models.ApiResponse
import org.vimteam.weatherstatistic.data.models.LocationDataResponse
import org.vimteam.weatherstatistic.data.models.WeatherDataResponse

class VisualcrossingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBodyString = response.body()?.string() ?: ""
        val emptyWeatherData = WeatherDataResponse(LocationDataResponse(ArrayList(), ""))
        when (response.code()) {
            200 -> {
                responseBodyString?.let {
                    if (responseBodyString.contains("errorCode")) {
                        return response.newBuilder().body(
                            createResponseBody(
                                Gson().toJson(
                                    ApiResponse(
                                        emptyWeatherData,
                                        ApiError(
                                            JSONObject(it).getString("errorCode"),
                                            JSONObject(it).getString("message")
                                        )
                                    )
                                )
                            )
                        )
                            .code(200)
                            .build()
                    }
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