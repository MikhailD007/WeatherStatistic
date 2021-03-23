package org.vimteam.weatherstatistic.ui

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import org.joda.time.LocalDate

class NetworkService() : Service() {

    var mBinder: IBinder? = null

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    fun getWeatherData(
        place: String,
        dateFrom: LocalDate,
        dateTo: LocalDate) {

    }

    internal class ServiceBinder : Binder() {
        private val networkService: NetworkService = NetworkService()

        fun getWeatherData(
            place: String,
            dateFrom: LocalDate,
            dateTo: LocalDate) {
            networkService.getWeatherData(place, dateFrom, dateTo)
        }

    }

}