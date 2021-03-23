package org.vimteam.weatherstatistic

import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    companion object {
        const val HISTORY_YEARS = 3
        const val MAX_DAYS = 3
    }

    override fun onCreate() {
        super.onCreate()
        val crashlitics = FirebaseCrashlytics.getInstance()
        with(crashlitics) {
            setUserId("")
            setCustomKey("UserName", "")
        }
        startKoin {
            androidContext(this@App)
            modules(
                MainModule.get()
            )
        }
    }
}