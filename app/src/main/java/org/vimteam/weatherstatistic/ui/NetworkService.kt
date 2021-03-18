package org.vimteam.weatherstatistic.ui

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NetworkService : Service() {
    var mBinder: IBinder? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}