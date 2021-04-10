package org.vimteam.weatherstatistic.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData

class ConnectivityListener(private val context: Context) {

    val isConnected = MutableLiveData<Boolean>()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getSystemService(context, ConnectivityManager::class.java)?.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected.postValue(true)
                }

                override fun onLost(network: Network) {
                    isConnected.postValue(false)
                }
            })
        } else {
            @Suppress("DEPRECATION")
            context.registerReceiver(
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        val isConn =
                            (context?.getSystemService(Context.CONNECTIVITY_SERVICE)
                                    as ConnectivityManager).let { connectivityManager ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    val networkCapabilities = connectivityManager.activeNetwork
                                    connectivityManager.getNetworkCapabilities(networkCapabilities)
                                        ?.let { activeNetwork ->
                                            when {
                                                activeNetwork.hasTransport(TRANSPORT_WIFI) -> true
                                                activeNetwork.hasTransport(TRANSPORT_CELLULAR) -> true
                                                activeNetwork.hasTransport(TRANSPORT_ETHERNET) -> true
                                                else -> false
                                            }
                                        } ?: false
                                } else {
                                    connectivityManager.activeNetworkInfo?.let {
                                        when (it.type) {
                                            ConnectivityManager.TYPE_WIFI -> true
                                            ConnectivityManager.TYPE_MOBILE -> true
                                            ConnectivityManager.TYPE_ETHERNET -> true
                                            else -> false
                                        }
                                    } ?: false
                                }
                            }
                        isConnected.postValue(isConn)
                    }
                },
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

}