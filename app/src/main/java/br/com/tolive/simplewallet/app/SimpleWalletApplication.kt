package br.com.tolive.simplewallet.app

import android.app.Application
import br.com.tolive.simplewallet.app.data.AppDatabase
import br.com.tolive.simplewallet.app.data.EntryRepository

class SimpleWalletApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    //val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { AppDatabase.getDatabase(this/*, applicationScope*/) }
    val repository by lazy { EntryRepository(database.entryDAO()) }
}