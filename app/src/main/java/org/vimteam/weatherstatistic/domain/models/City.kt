package org.vimteam.weatherstatistic.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val googleUID: String,
    val name: String,
    val lat: Double,
    val lon: Double
): Parcelable
