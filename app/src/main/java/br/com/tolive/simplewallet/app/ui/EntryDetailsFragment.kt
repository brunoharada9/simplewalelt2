package br.com.tolive.simplewallet.app.ui

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.databinding.FragmentEntryDetailsBinding
import br.com.tolive.simplewallet.app.utils.Utils


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EntryDetailsFragment : Fragment() {

    private var _binding: FragmentEntryDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEntryDetailsBinding.inflate(inflater, container, false)

        val transitionName = arguments?.getString(NAME_KEY)

        ViewCompat.setTransitionName(binding.entryCard, transitionName)

        return binding.root
    }

    companion object {
        private const val NAME_KEY = "key"

        fun getBundle(transitionName: String?): Bundle {
            val args = Bundle()
            args.putString(NAME_KEY, transitionName)
            return args
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entry: Entry? = getParcelable()

        if (entry != null) {
            if (entry.type == Entry.TYPE_GAIN){
                binding.entryBackground.setBackgroundResource(R.color.green)
            } else if (entry.type == Entry.TYPE_EXPENSE) {
                binding.entryBackground.setBackgroundResource(R.color.red)
            }

            binding.entryDescription.text = entry.description.toString()
            binding.entryDescription.movementMethod = ScrollingMovementMethod()
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
        _binding = null
    }
}