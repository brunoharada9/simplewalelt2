package br.com.tolive.simplewallet.app.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.databinding.FragmentEntryDetailsBinding
import br.com.tolive.simplewallet.app.utils.Utils

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EntryDetailsFragment : Fragment() {

    private var _binding: FragmentEntryDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).fab.hide()

        _binding = FragmentEntryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entry: Entry? = getParcelable()

        if (entry != null) {
            if (entry.type == Entry.TYPE_GAIN){
                binding.entryBackground.setBackgroundResource(android.R.color.holo_green_light)
            } else if (entry.type == Entry.TYPE_EXPENSE) {
                binding.entryBackground.setBackgroundResource(android.R.color.holo_red_light)
            }

            binding.entryDescription.text = entry.description.toString()
            binding.entryValue.text = Utils.getEntryValueFormatted(entry)
            binding.entryDate.text = entry.entryDate.toString()
        }
    }

    private fun getParcelable(): Entry? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arguments?.getParcelable(EntryListFragment.KEY_ENTRY_DETAILS, Entry::class.java)
            else -> @Suppress("DEPRECATION")
                arguments?.getParcelable(EntryListFragment.KEY_ENTRY_DETAILS) as? Entry
        }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).fab.show()
        _binding = null
    }
}