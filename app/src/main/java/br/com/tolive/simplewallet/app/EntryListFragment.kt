package br.com.tolive.simplewallet.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.tolive.simplewallet.app.adapters.EntryListAdapter
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.databinding.FragmentEntryListBinding
import br.com.tolive.simplewallet.app.utils.Utils
import br.com.tolive.simplewallet.app.viewmodel.EntryListViewModel
import br.com.tolive.simplewallet.app.viewmodel.EntryViewModelFactory
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EntryListFragment : Fragment(), MainActivity.OnAddEntryListener, EntryListAdapter.OnEntryClickListener {

    private var _binding: FragmentEntryListBinding? = null

    private val viewModel: EntryListViewModel by viewModels{
        EntryViewModelFactory((activity?.application as SimpleWalletApplication).repository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEntryListBinding.inflate(inflater, container, false)

        val adapter = EntryListAdapter()
        //subscribeUi(adapter)
        binding.entryList.adapter = adapter
        adapter.setOnEntryLongClick(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.allEntries.observe(viewLifecycleOwner) { entries ->
            // Update the cached copy of the words in the adapter.
            entries.let { adapter.submitList(it) }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEntryLongClick(entry: Entry) {
        // TODO: Create a custom alert dialog
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(R.string.remove_entry_dialog_tittle)
        builder?.setMessage("Do you want to remove this entry?\n\n" +
                "Description\n" +
                "${entry.description}\n\n" +
                "Value\n" +
                Utils.getEntryValueFormatted(entry)
        )
        builder?.setPositiveButton(android.R.string.ok) { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                Toast.makeText(context, R.string.remove_entry_toast, Toast.LENGTH_SHORT).show()
                viewModel.delete(entry)
            }
        }
        builder?.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder?.show()

    }

    override fun onAddEntry(entry: Entry) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.insert(entry)
        }
    }
}