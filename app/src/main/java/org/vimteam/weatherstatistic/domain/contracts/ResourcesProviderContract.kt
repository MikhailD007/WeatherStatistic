package org.vimteam.weatherstatistic.domain.contracts

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourcesProviderContract {

    fun getString(@StringRes id: Int): String

    fun getDrawable(@DrawableRes id: Int): Drawable?
}