package br.com.tolive.simplewallet.app.ui

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.databinding.FragmentTransactionDetailsBinding
import br.com.tolive.simplewallet.app.utils.Utils
import br.com.tolive.simplewallet.app.viewmodel.TransactionDetailsViewModel
import br.com.tolive.simplewallet.app.viewmodel.TransactionDetailsViewModelFactory


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TransactionDetailsFragment : BaseMenuAnimFragment(), RemoveTransactionDialog.OnRemoveTransactionListener {

    private var _binding: FragmentTransactionDetailsBinding? = null
    private var transaction: Transaction? = null

    private val viewModel: TransactionDetailsViewModel by viewModels {
        TransactionDetailsViewModelFactory((activity?.application as SimpleWalletApplication).repository)
    }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuRes = R.menu.menu_details

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_edit -> {
                // TODO:
                Toast.makeText(activity, "Edit", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_delete -> {
                if (transaction != null) {
                    RemoveTransactionDialog(
                        transaction!!,
                        this
                    ).show(
                        parentFragmentManager,
                        RemoveTransactionDialog.TAG
                    )
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)

        val transitionName = arguments?.getString(NAME_KEY)

        ViewCompat.setTransitionName(binding.transactionCard, transitionName)

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

        transaction = getParcelable()

        if (transaction != null) {
            if (transaction!!.type == Transaction.TYPE_GAIN) {
                binding.transactionBackground.setBackgroundResource(R.color.green)
            } else if (transaction!!.type == Transaction.TYPE_EXPENSE) {
                binding.transactionBackground.setBackgroundResource(R.color.red)
            }

            binding.transactionDescription.text = transaction!!.description.toString()
            binding.transactionDescription.movementMethod = ScrollingMovementMethod()
            binding.transactionValue.text = Utils.getTransactionValueFormatted(transaction!!)
            binding.transactionDate.text = transaction!!.transactionDate.toString()
        }
    }

    override fun onRemoveTransaction(transaction: Transaction) {
        viewModel.delete(transaction)
        findNavController().popBackStack()
    }

    private fun getParcelable(): Transaction? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arguments?.getParcelable(
                    TransactionListFragment.KEY_ENTRY_DETAILS,
                    Transaction::class.java
                )
            else -> @Suppress("DEPRECATION")
            arguments?.getParcelable(TransactionListFragment.KEY_ENTRY_DETAILS) as? Transaction
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}