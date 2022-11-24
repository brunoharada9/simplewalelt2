package br.com.tolive.simplewallet.app.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddEntryDialog(var addEntryListener: MainActivity.OnAddEntryListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
        val inflater = layoutInflater
        builder.setTitle(R.string.add_entry_dialog_title)

        val dialogLayout = inflater.inflate(R.layout.add_entry_dialog, null)
        val editTextDescription  = dialogLayout.findViewById<EditText>(R.id.edit_text_description)
        val editTextValue  = dialogLayout.findViewById<EditText>(R.id.edit_text_value)
        val datePicker  = dialogLayout.findViewById<DatePicker>(R.id.date_picker)

        val radioGroup  = dialogLayout.findViewById<RadioGroup>(R.id.radio_group)
        // TODO: change Dialog color based on entry type
        val radioButtonGain  = dialogLayout.findViewById<RadioButton>(R.id.radio_gain)
        val radioButtonExpense  = dialogLayout.findViewById<RadioButton>(R.id.radio_expense)

        builder.setView(dialogLayout)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val entryType = getEntryType(radioGroup)
            val value = Utils.convertEditTextToDouble(editTextValue)
            val entryDate =
                Entry.Date(datePicker.dayOfMonth, datePicker.month, datePicker.year)

            val entry = Entry(value, entryType, editTextDescription.text.toString(), entryDate)
            addEntryListener.onAddEntry(entry)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }

    private fun getEntryType(radioGroup: RadioGroup): Int {
        var entryType = Entry.TYPE_GAIN
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_gain -> entryType = Entry.TYPE_GAIN
            R.id.radio_expense -> entryType = Entry.TYPE_EXPENSE
        }
        return entryType
    }

    companion object {
        const val TAG: String = "AddEntryDialog"
    }
}