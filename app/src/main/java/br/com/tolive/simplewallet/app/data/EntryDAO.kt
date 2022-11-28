package br.com.tolive.simplewallet.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insertEntry(vararg entry: Entry)

    @Delete
    fun delete(entry: Entry)

    @Query("DELETE FROM entries")
    fun deleteAll()

    @Query("SELECT * FROM entries")
    fun getEntries(): Flow<List<Entry>>

    @Query("SELECT SUM(value) FROM entries")
    fun getValueSum(): Double

    @Query("SELECT * FROM entries WHERE year = :year")
    fun getByYear(year: Int):Flow<List<Entry>>

    @Query("SELECT * FROM entries WHERE month = :month")
    fun getByMonth(month: Int):Flow<List<Entry>>
}