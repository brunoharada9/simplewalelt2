package br.com.tolive.simplewallet.app.viewmodel

import androidx.lifecycle.ViewModel
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.data.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionDetailsViewModel constructor (
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun update(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.update(transaction)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun delete(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        transactionRepository.delete(transaction)
    }
}