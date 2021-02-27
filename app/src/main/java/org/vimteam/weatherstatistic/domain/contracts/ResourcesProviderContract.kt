package org.vimteam.weatherstatistic.domain.contracts

interface ResourcesProviderContract {

    fun getString(id: Int): String

    fun getString(stringName: String): String
}