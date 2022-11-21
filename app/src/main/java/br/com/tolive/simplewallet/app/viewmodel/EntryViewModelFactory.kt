package br.com.tolive.simplewallet.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.tolive.simplewallet.app.data.EntryRepository

class EntryViewModelFactory(private val repository: EntryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntryListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}