package br.com.tolive.simplewallet.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.tolive.simplewallet.app.ui.EntryListFragment
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.ui.RemoveEntryDialog
import br.com.tolive.simplewallet.app.utils.Utils
import com.google.android.material.card.MaterialCardView

/**
 * Adapter for the [RecyclerView] in [EntryListFragment].
 */
class EntryListAdapter (var onEntryLongClick : OnEntryClickListener) : ListAdapter<Entry, EntryListAdapter.EntryListViewHolder>(EntriesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryListViewHolder {
        val entryListViewHolder: EntryListViewHolder = EntryListViewHolder.create(parent)
        entryListViewHolder.setOnEntryLongClick(onEntryLongClick)

        return entryListViewHolder
    }

    override fun onBindViewHolder(holder: EntryListViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current)
    }

    interface OnEntryClickListener {
        fun onEntryLongClick(entry: Entry)
    }

    class EntryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val entryCard: MaterialCardView = itemView.findViewById(R.id.entry_card)
        private val entryBackground: RelativeLayout = itemView.findViewById(R.id.entry_background)

        private val entryDescription: TextView = itemView.findViewById(R.id.entry_description)
        private val entryValue: TextView = itemView.findViewById(R.id.summary_value)
        private val entryDate: TextView = itemView.findViewById(R.id.summary_month)

        private var onEntryLongClick: OnEntryClickListener? = null

        fun setOnEntryLongClick(onEntryLongClick: OnEntryClickListener) {
            this.onEntryLongClick = onEntryLongClick
        }

        fun bind(currentEntry: Entry) {
            if (currentEntry.type == Entry.TYPE_GAIN){
                entryBackground.setBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_light))
            } else if (currentEntry.type == Entry.TYPE_EXPENSE) {
                entryBackground.setBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_light))
            }

            val valueCurrency = Utils.getEntryValueFormatted(currentEntry)
            entryDescription.text = currentEntry.description
            entryValue.text = valueCurrency
            entryDate.text = currentEntry.entryDate.toString()

            entryCard.setOnLongClickListener{
                RemoveEntryDialog(currentEntry, onEntryLongClick).show((itemView.context as AppCompatActivity).supportFragmentManager, RemoveEntryDialog.TAG)
                return@setOnLongClickListener true
            }
        }

        companion object {
            fun create(parent: ViewGroup): EntryListViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.entry_list_item, parent, false)
                return EntryListViewHolder(view)
            }
        }
    }

    class EntriesComparator : DiffUtil.ItemCallback<Entry>() {
        override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
            return oldItem.value == newItem.value
        }
    }

}