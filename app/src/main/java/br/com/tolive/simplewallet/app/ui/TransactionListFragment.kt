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
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.adapters.TransactionListAdapter
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.databinding.FragmentTransactionListBinding
import br.com.tolive.simplewallet.app.viewmodel.TransactionListViewModel
import br.com.tolive.simplewallet.app.viewmodel.TransactionViewModelFactory
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TransactionListFragment : Fragment(), MainActivity.OnAddTransactionListener, TransactionListAdapter.OnTransactionClickListener {

    private var _binding: FragmentTransactionListBinding? = null
    private lateinit var _activity: MainActivity

    private val viewModel: TransactionListViewModel by viewModels{
        TransactionViewModelFactory((activity?.application as SimpleWalletApplication).repository)
    }

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _activity = activity as MainActivity
        _activity.showSummaryAndFab()

        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)

        val adapter = TransactionListAdapter(this)
        binding.transactionList.adapter = adapter

        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            // Update the cached copy of the transactions in the adapter.
            transactions.let {
                adapter.submitList(it) {
                    binding.transactionList.scrollToPosition(transactions.size - 1)
                }

                _activity.updateSummary(viewModel.sum)
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

    override fun onTransactionClick(transaction: Transaction, transactionCard: View) {
        val args = TransactionDetailsFragment.getBundle(transactionCard.transitionName)
        args.putParcelable(KEY_ENTRY_DETAILS, transaction)
        val extras = FragmentNavigatorExtras(
            transactionCard to transactionCard.transitionName
        )
        _activity.hideSummaryAndFab()
        findNavController().navigate(R.id.action_TransactionListFragment_to_TransactionDetailsFragment, args, null, extras)
    }

    override fun onTransactionLongClick(transaction: Transaction) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.delete(transaction)
        }
    }

    override fun onAddTransaction(transaction: Transaction) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.insert(transaction)
        }
    }

    companion object {
        const val KEY_ENTRY_DETAILS = "key_transaction_details"
    }
}