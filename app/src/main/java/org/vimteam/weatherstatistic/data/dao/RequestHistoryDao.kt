package org.vimteam.weatherstatistic.data.dao

import androidx.room.*
import org.vimteam.weatherstatistic.data.models.dao.RequestHistoryEntity

@Dao
interface RequestHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: RequestHistoryEntity)

    @Delete
    fun delete(entity: RequestHistoryEntity)

    @Query("SELECT * FROM RequestHistoryEntity")
    fun allRecords(): List<RequestHistoryEntity>
}