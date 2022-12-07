package br.com.tolive.simplewallet.app.viewmodel

import androidx.lifecycle.*
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
    val allTransactions: LiveData<List<Transaction>> = transactionRepository.allTransactions.asLiveData()

    var sum = 0.0

    init {
        CoroutineScope(Dispatchers.IO).launch {
            sum = transactionRepository.getValueSum()
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.insert(transaction)
        //sum += transaction.value
        sum = transactionRepository.getValueSum()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun delete(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.delete(transaction)
        //sum -= transaction.value
        sum = transactionRepository.getValueSum()
    }
}