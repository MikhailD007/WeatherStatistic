package org.vimteam.weatherstatistic.domain.models

data class City(
    val googleUID: String,
    val name: String,
    val lat: Double,
    val lon: Double
)
