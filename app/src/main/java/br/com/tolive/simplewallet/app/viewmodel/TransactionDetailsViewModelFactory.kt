package br.com.tolive.simplewallet.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.tolive.simplewallet.app.data.TransactionRepository

class TransactionDetailsViewModelFactory(private val repository: TransactionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}