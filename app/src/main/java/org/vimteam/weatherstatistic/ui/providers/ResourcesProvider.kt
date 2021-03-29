package org.vimteam.weatherstatistic.ui.providers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract

class ResourcesProvider(private val context: Context) : ResourcesProviderContract{

    override fun getString(@StringRes id: Int) = context.getString(id)

    override fun getDrawable(id: Int): Drawable?  = context.getDrawable(id)

}