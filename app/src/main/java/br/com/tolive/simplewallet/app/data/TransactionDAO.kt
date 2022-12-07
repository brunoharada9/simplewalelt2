package br.com.tolive.simplewallet.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insertTransaction(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions")
    fun deleteAll()

    @Query("SELECT * FROM transactions")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT SUM(value) FROM transactions")
    fun getValueSum(): Double

    @Query("SELECT * FROM transactions WHERE year = :year")
    fun getByYear(year: Int):Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE month = :month")
    fun getByMonth(month: Int):Flow<List<Transaction>>
}