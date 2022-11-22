package br.com.tolive.simplewallet.app.viewmodel

import androidx.lifecycle.*
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.data.EntryRepository
import br.com.tolive.simplewallet.app.EntryListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel for [EntryListFragment].
 */
class EntryListViewModel constructor (
    private val entryRepository: EntryRepository
) : ViewModel() {

    // Using LiveData and caching what allEntries returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allEntries: LiveData<List<Entry>> = entryRepository.allEntries.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(entry: Entry) = CoroutineScope(Dispatchers.IO).launch {
        entryRepository.insert(entry)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun delete(entry: Entry) = CoroutineScope(Dispatchers.IO).launch {
        entryRepository.delete(entry)
    }
}