package br.com.tolive.simplewallet.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.tolive.simplewallet.app.ui.TransactionListFragment
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.utils.Utils
import com.google.android.material.card.MaterialCardView

/**
 * Adapter for the [RecyclerView] in [TransactionListFragment].
 */
class TransactionListAdapter(var onTransactionClickListener: OnTransactionClickListener) :
    ListAdapter<Transaction, TransactionListAdapter.TransactionListViewHolder>(
        TransactionsComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val transactionListViewHolder: TransactionListViewHolder =
            TransactionListViewHolder.create(parent)
        transactionListViewHolder.setOnTransactionLongClick(onTransactionClickListener)

        return transactionListViewHolder
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current, position)
    }

    interface OnTransactionClickListener {
        fun onTransactionClick(transaction: Transaction, transactionCard: View)
        fun onTransactionLongClick(transaction: Transaction)
    }

    class TransactionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val transactionCard: MaterialCardView = itemView.findViewById(R.id.transaction_card)
        private val transactionBackground: RelativeLayout =
            itemView.findViewById(R.id.transaction_background)

        private val transactionDescription: TextView =
            itemView.findViewById(R.id.transaction_description)
        private val transactionValue: TextView = itemView.findViewById(R.id.transaction_value)
        private val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)

        private var onTransactionLongClick: OnTransactionClickListener? = null

        fun setOnTransactionLongClick(onTransactionLongClick: OnTransactionClickListener) {
            this.onTransactionLongClick = onTransactionLongClick
        }

        fun bind(currentTransaction: Transaction, position: Int) {
            if (currentTransaction.type == Transaction.TYPE_GAIN) {
                transactionBackground.setBackgroundResource(R.color.green)
            } else if (currentTransaction.type == Transaction.TYPE_EXPENSE) {
                transactionBackground.setBackgroundResource(R.color.red)
            }

            val valueCurrency = Utils.getTransactionValueFormatted(currentTransaction)
            transactionDescription.text = currentTransaction.description
            transactionValue.text = valueCurrency
            transactionDate.text = currentTransaction.transactionDate.toString()

            transactionCard.setOnClickListener {
                onTransactionLongClick?.onTransactionClick(currentTransaction, transactionCard)
            }

            transactionCard.setOnLongClickListener {
                onTransactionLongClick?.onTransactionLongClick(currentTransaction)
                return@setOnLongClickListener true
            }

            ViewCompat.setTransitionName(transactionCard, "transaction_card_list_$position")
        }

        companion object {
            fun create(parent: ViewGroup): TransactionListViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.transaction_list_item, parent, false)
                return TransactionListViewHolder(view)
            }
        }
    }

    class TransactionsComparator : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.value == newItem.value
        }
    }

}