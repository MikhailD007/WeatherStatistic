package org.vimteam.weatherstatistic.ui.providers

import android.content.Context
import androidx.annotation.StringRes
import org.vimteam.weatherstatistic.domain.contracts.ResourcesProviderContract

class ResourcesProvider(private val context: Context) : ResourcesProviderContract{

    override fun getString(@StringRes id: Int) = context.getString(id)

    override fun getString(stringName: String) = context.getString(context.resources.getIdentifier(stringName, "string", context.packageName))

}