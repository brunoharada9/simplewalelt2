package br.com.tolive.simplewallet.app.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.adapters.TransactionListAdapter
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RemoveTransactionDialog(var transaction: Transaction, var onTransactionLongClick: TransactionListAdapter.OnTransactionClickListener?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
        builder.setTitle(R.string.remove_transaction_dialog_tittle)
        // TODO create a layout for this message
        builder.setMessage("Do you want to remove this transaction?\n\n" +
                "Description\n" +
                "${transaction.description}\n\n" +
                "Value\n" +
                "${Utils.getTransactionValueFormatted(transaction)}\n\n" +
                "Date\n" +
                transaction.transactionDate
        )
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            Toast.makeText(context, R.string.remove_transaction_toast, Toast.LENGTH_SHORT).show()
            onTransactionLongClick?.onTransactionLongClick(transaction)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }

    companion object {
        const val TAG: String = "RemoveTransactionDialog"
    }
}