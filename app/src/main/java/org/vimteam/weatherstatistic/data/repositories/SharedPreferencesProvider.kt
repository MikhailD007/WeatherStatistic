package org.vimteam.weatherstatistic.data.repositories

import android.content.Context
import androidx.preference.PreferenceManager
import org.vimteam.weatherstatistic.base.string
import org.vimteam.weatherstatistic.domain.contracts.SharedPreferencesContract

class SharedPreferencesProvider(context: Context): SharedPreferencesContract {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override var place by preferences.string("")

}