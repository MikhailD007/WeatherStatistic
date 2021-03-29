package org.vimteam.weatherstatistic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.vimteam.weatherstatistic.data.converters.LocalDateConverterDB
import org.vimteam.weatherstatistic.data.dao.RequestHistoryDao
import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity

@Database(
    entities = [
        RequestHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverterDB::class)
abstract class WeatherDB : RoomDatabase() {

    abstract fun requestHistoryDao(): RequestHistoryDao

}