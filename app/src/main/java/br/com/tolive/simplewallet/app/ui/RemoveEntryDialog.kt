package br.com.tolive.simplewallet.app.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.adapters.EntryListAdapter
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RemoveEntryDialog(var entry: Entry, var onEntryLongClick: EntryListAdapter.OnEntryClickListener?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
        builder.setTitle(R.string.remove_entry_dialog_tittle)
        builder.setMessage("Do you want to remove this entry?\n\n" +
                "Description\n" +
                "${entry.description}\n\n" +
                "Value\n" +
                Utils.getEntryValueFormatted(entry)
        )
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            Toast.makeText(context, R.string.remove_entry_toast, Toast.LENGTH_SHORT).show()
            onEntryLongClick?.onEntryLongClick(entry)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }

    companion object {
        const val TAG: String = "RemoveEntryDialog"
    }
}