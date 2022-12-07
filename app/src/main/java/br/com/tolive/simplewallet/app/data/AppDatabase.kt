package br.com.tolive.simplewallet.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDAO(): TransactionDAO

    /*// Just for testing
    private class TransactionDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    //populateDatabase(database.transactionDAO())
                }
            }
        }

        fun populateDatabase(transactionDAO: TransactionDAO) {
            // Delete all content here.
            transactionDAO.deleteAll()

            // Add Test transactions.
            var transaction = Transaction(1.2, Transaction.TYPE_GAIN, "Test 1")
            transactionDAO.insertTransaction(transaction)
            transaction = Transaction(2.2, Transaction.TYPE_EXPENSE, "Test 2")
            transactionDAO.insertTransaction(transaction)
        }
    }*/

    companion object {
    // Singleton prevents multiple instances of database opening at the
    // same time.
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(
        context: Context,
        //scope: CoroutineScope
    ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )//.addCallback(TransactionDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}