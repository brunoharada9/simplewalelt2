package br.com.tolive.simplewallet.app.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.adapters.TransactionListAdapter
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.databinding.FragmentTransactionListBinding
import br.com.tolive.simplewallet.app.viewmodel.TransactionListViewModel
import br.com.tolive.simplewallet.app.viewmodel.TransactionViewModelFactory


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TransactionListFragment : BaseMenuAnimFragment(), MainActivity.OnMainActivityListener,
    TransactionListAdapter.OnTransactionClickListener {

    private var _binding: FragmentTransactionListBinding? = null

    private val viewModel: TransactionListViewModel by viewModels {
        TransactionViewModelFactory((activity?.application as SimpleWalletApplication).repository)
    }

    private val binding get() = _binding!!

    // Control if transaction list go to last position
    // it should not go to last position if coming from details fragment because of the animation
    private var sGoToLastPosition: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        menuRes = R.menu.menu_transaction_list

        mainActivity.showSummaryAndFab()

        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)

        val adapter = TransactionListAdapter(this)
        binding.transactionList.adapter = adapter

        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.transactions.removeObservers(viewLifecycleOwner)

        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            // Update the cached copy of the transactions in the adapter.
            transactions.let { it ->
                adapter.submitList(it) {
                    if (sGoToLastPosition) {
                        binding.transactionList.scrollToPosition(transactions.size - 1)
                    }
                }

                mainActivity.updateSummarySum(transactions.sumOf { it.value })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_filter -> {
                FilterDialog(this).show(parentFragmentManager, FilterDialog.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTransactionClick(transaction: Transaction, transactionCard: View) {
        val args = TransactionDetailsFragment.getBundle(transactionCard.transitionName)
        args.putParcelable(KEY_ENTRY_DETAILS, transaction)
        val extras = FragmentNavigatorExtras(
            transactionCard to transactionCard.transitionName
        )
        mainActivity.hideSummaryAndFab()
        sGoToLastPosition = false
        findNavController().navigate(
            R.id.action_TransactionListFragment_to_TransactionDetailsFragment,
            args,
            null,
            extras
        )
    }

    override fun onTransactionLongClick(transaction: Transaction) {
        viewModel.delete(transaction)
        sGoToLastPosition = true
    }

    override fun onAddTransaction(transaction: Transaction) {
        viewModel.insert(transaction)
        sGoToLastPosition = true
    }

    override fun onFilterApplied(month: Int, year: Int) {
        viewModel.setMonth(month, year)
        // Needed this to refresh the listview, don't know why
        mainActivity.updateSummaryMonth(month, year)
        sGoToLastPosition = true
        findNavController().navigate(R.id.action_refresh_TransactionListFragment)
    }

    companion object {
        const val KEY_ENTRY_DETAILS = "key_transaction_details"
    }
}