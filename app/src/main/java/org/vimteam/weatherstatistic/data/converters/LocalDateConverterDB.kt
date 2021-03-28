package org.vimteam.weatherstatistic.data.converters

import androidx.room.TypeConverter
import org.joda.time.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LocalDateConverterDB {

    @TypeConverter
    fun fromLocateDate(date: LocalDate?) = date?.toString()

    @TypeConverter
    fun toLocateDate(dateStr: String?): LocalDate? = dateStr?.let {
        try {
            LocalDate.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

}