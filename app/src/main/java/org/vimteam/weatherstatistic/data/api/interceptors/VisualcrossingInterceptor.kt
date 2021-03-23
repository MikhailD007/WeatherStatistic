package org.vimteam.weatherstatistic.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class VisualcrossingInterceptor :Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        when (response.code()) {
            200 -> {
//                val responseString = response.body()?.string() ?:""
//                if (responseString.contains("errorCode")) {
//                    //TODO handle server error response HOW?
//                }
            }
            else -> {
                //TODO handle standard error response HOW?
            }
        }
        return response
    }

}