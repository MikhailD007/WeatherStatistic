package org.vimteam.weatherstatistic.data.models.dao

import androidx.room.Entity
import androidx.room.TypeConverter
import org.joda.time.LocalDate
import org.vimteam.weatherstatistic.data.converters.LocalDateConverterDB

@Entity(
    primaryKeys = [
        "place",
        "date"
    ]
)

data class RequestHistoryEntity(

    val place: String,
    val date: LocalDate,
    val minTemperature: Float,
    val maxTemperature: Float

)
