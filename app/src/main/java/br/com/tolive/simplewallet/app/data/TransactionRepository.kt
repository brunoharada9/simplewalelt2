package br.com.tolive.simplewallet.app.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.util.*

class TransactionRepository constructor(
        private val transactionDAO: TransactionDAO
    ) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    var transactions: Flow<List<Transaction>> = setByMonth(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR))

    //fun getTransaction(transactionId: String) = transactionDAO.getTransaction(transactionId)

    fun getByYear(year: Int) =
        transactionDAO.getByYear(year)

    fun setByMonth(month: Int, year: Int): Flow<List<Transaction>> {
        return setByMonth(month, year) {}
    }

    fun setByMonth(month: Int, year: Int, callback: (transactions: Flow<List<Transaction>>) -> Unit): Flow<List<Transaction>> {
        transactions = transactionDAO.getByMonth(month, year)
        callback(transactions)

        return transactions
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    fun insert(transaction: Transaction) {
        transactionDAO.insertTransaction(transaction)
    }

    fun update(transaction: Transaction) = transactionDAO.update(transaction)

    fun delete(transaction: Transaction) = transactionDAO.delete(transaction)
}