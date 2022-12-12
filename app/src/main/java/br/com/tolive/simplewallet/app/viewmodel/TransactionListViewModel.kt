package br.com.tolive.simplewallet.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.data.TransactionRepository
import br.com.tolive.simplewallet.app.ui.TransactionListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel for [TransactionListFragment].
 */
class TransactionListViewModel constructor (
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // Using LiveData and caching what allTransactions returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    var transactions: LiveData<List<Transaction>> = transactionRepository.transactions.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun setMonth(month: Int, year: Int) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.setByMonth(month, year) {
            transactions = it.asLiveData()
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.insert(transaction)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun delete(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.delete(transaction)
    }
}