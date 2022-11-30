package br.com.tolive.simplewallet.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.tolive.simplewallet.app.R
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

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).fab.show()

        _binding = FragmentEntryListBinding.inflate(inflater, container, false)

        val linearLayoutManager = LinearLayoutManager(activity)
        // TODO: ideally new entries should be on top,
        //  but this is messing up with the returning transaction animation
        //  so let's comment for now
        //linearLayoutManager.reverseLayout = true
        //linearLayoutManager.stackFromEnd = true
        binding.entryList.layoutManager = linearLayoutManager

        val adapter = EntryListAdapter(this)
        binding.entryList.adapter = adapter

        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.allEntries.observe(viewLifecycleOwner) { entries ->
            // Update the cached copy of the entries in the adapter.
            entries.let {
                adapter.submitList(it)

                binding.summaryValue.text = Utils.getValueFormatted(viewModel.sum)
                if (viewModel.sum < 0) {
                    binding.layoutSummary.setBackgroundResource(android.R.color.holo_red_light)
                } else {
                    binding.layoutSummary.setBackgroundResource(android.R.color.holo_green_light)
                }
            }
        }

        // Necessary for sharedElementReturnTransition to work
        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEntryClick(entry: Entry, entryCard: View) {
        val args = EntryDetailsFragment.getBundle(entryCard.transitionName)
        args.putParcelable(KEY_ENTRY_DETAILS, entry)
        val extras = FragmentNavigatorExtras(
            entryCard to entryCard.transitionName
        )
        (activity as MainActivity).fab.hide()
        findNavController().navigate(R.id.action_EntryListFragment_to_EntryDetailsFragment, args, null, extras)
    }

    override fun onEntryLongClick(entry: Entry) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.delete(entry)
        }
    }

    override fun onAddEntry(entry: Entry) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.insert(entry)
        }
    }

    companion object {
        const val KEY_ENTRY_DETAILS = "key_entry_details"
    }
}