package org.vimteam.weatherstatistic.ui.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import org.koin.android.ext.android.inject
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.domain.contracts.ApiRepositoryContract

class NetworkService() : Service() {

    private val repo: ApiRepositoryContract by inject()

    private val ID_NOTIFICATION = 801
    private val SERVICE_ID = "WeatherHistoryService_vimteam.org"

    private val mManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    @Suppress("DEPRECATION")
    private val mBuilder: NotificationCompat.Builder by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationCompat.Builder(
            applicationContext,
            SERVICE_ID
        ) else NotificationCompat.Builder(applicationContext)
    }


    companion object {
        const val DATE_FROM = "DATE_FROM"
        const val DATE_TO = "DATE_TO"
        const val PLACE = "PLACE"
        const val ACTION_INTENT = "org.vimteam.weatherstatistic.exchange"
        const val RESULT_DATA_TAG = "org.vimteam.weatherstatistic.exchange.result"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SERVICE_ID,
                getString(R.string.service_name),
                NotificationManager.IMPORTANCE_NONE
            )
            channel.description = getString(R.string.service_description)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            if (mManager.getNotificationChannel(SERVICE_ID) == null) mManager.createNotificationChannel(channel)
        }
        mBuilder
            .setContentTitle(getString(R.string.service_name))
            .setContentText(getString(R.string.idle_state))
            .setSmallIcon(R.drawable.ic_exchange)
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.idle_state)))
            .setOngoing(true)

        val notification: Notification = mBuilder.build()
        startForeground(ID_NOTIFICATION, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val place = it.getStringExtra(PLACE) ?: ""
            if (place.isNotEmpty()) {
                val dateFrom = it.getSerializableExtra(DATE_FROM) as LocalDate
                val dateTo = it.getSerializableExtra(DATE_TO) as LocalDate
                GlobalScope.launch {
                    getWeatherData(place, dateFrom, dateTo)
                }
            }

        }
        return START_STICKY
    }

    private suspend fun getWeatherData(
        place: String,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ) {
        notify(getString(R.string.collecting_data_from_server),getString(R.string.request_params,place,dateFrom.toString(),dateTo.toString()))
        repo.getWeatherData(
            place,
            dateFrom,
            dateTo,
            { notify(getString(R.string.exchange_error, it.message ?:it.toString())) },
            { weatherStat ->
                sendBroadcast(
                    Intent(ACTION_INTENT).apply {
                        this.putParcelableArrayListExtra(RESULT_DATA_TAG, weatherStat)
                    }
                )
                notify(getString(R.string.idle_state))
            }
        )
    }

    private fun notify(status: String, description: String = status) {
        mBuilder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(status)
        )
        mBuilder.setContentText(description)
        mManager.notify(ID_NOTIFICATION, mBuilder.build())
    }

}