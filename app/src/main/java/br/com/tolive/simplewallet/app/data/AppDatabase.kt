package br.com.tolive.simplewallet.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Entry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entryDAO(): EntryDAO

    /*// Just for testing
    private class EntryDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    //populateDatabase(database.entryDAO())
                }
            }
        }

        fun populateDatabase(entryDAO: EntryDAO) {
            // Delete all content here.
            entryDAO.deleteAll()

            // Add Test entries.
            var entry = Entry(1.2, Entry.TYPE_GAIN, "Test 1")
            entryDAO.insertEntry(entry)
            entry = Entry(2.2, Entry.TYPE_EXPENSE, "Test 2")
            entryDAO.insertEntry(entry)
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
                )//.addCallback(EntryDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}