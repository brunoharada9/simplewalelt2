package br.com.tolive.simplewallet.app.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TransactionRepository constructor(private val transactionDAO: TransactionDAO) {

    fun delete(transaction: Transaction) = transactionDAO.delete(transaction)

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTransactions: Flow<List<Transaction>> = transactionDAO.getTransactions()

    fun getValueSum(): Double {
        return transactionDAO.getValueSum()
    }

    //fun getTransaction(transactionId: String) = transactionDAO.getTransaction(transactionId)

    fun getByYear(year: Int) =
        transactionDAO.getByYear(year)

    fun getByMonth(month: Int) =
        transactionDAO.getByMonth(month)

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    fun insert(transaction: Transaction) {
        transactionDAO.insertTransaction(transaction)
    }
}