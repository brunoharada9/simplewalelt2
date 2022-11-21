package br.com.tolive.simplewallet.app.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class EntryRepository constructor(private val entryDAO: EntryDAO) {

    fun delete(entry: Entry) = entryDAO.delete(entry)

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allEntries: Flow<List<Entry>> = entryDAO.getEntries()

    //fun getEntry(entryId: String) = entryDAO.getEntry(entryId)

    fun getByYear(year: Int) =
        entryDAO.getByYear(year)

    fun getByMonth(month: Int) =
        entryDAO.getByMonth(month)

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    fun insert(entry: Entry) {
        entryDAO.insertEntry(entry)
    }
}