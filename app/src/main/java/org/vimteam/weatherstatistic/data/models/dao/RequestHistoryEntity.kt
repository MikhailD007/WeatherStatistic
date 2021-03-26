package org.vimteam.weatherstatistic.data.models.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = [
    "place",
    "date"
])
data class RequestHistoryEntity(

    val place: String,
    val date: String,
    val minTemperature: Float,
    val maxTemperature: Float

)
