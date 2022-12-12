package br.com.tolive.simplewallet.app.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import br.com.tolive.simplewallet.app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class FilterDialog(var onMainActivityListener: MainActivity.OnMainActivityListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
        val inflater = layoutInflater
        builder.setTitle(R.string.filter_dialog_title)

        val cal: Calendar = Calendar.getInstance()

        val dialogLayout = inflater.inflate(R.layout.filter_dialog, null)
        val monthPicker  = dialogLayout.findViewById<NumberPicker>(R.id.spinner_month)
        val yearPicker  = dialogLayout.findViewById<NumberPicker>(R.id.spinner_year)

        monthPicker.minValue = MIN_MONTH
        monthPicker.maxValue = MAX_MONTH
        monthPicker.value = cal.get(Calendar.MONTH) + 1

        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = cal.get(Calendar.YEAR)

        builder.setView(dialogLayout)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val month = monthPicker.value - 1
            val year = yearPicker.value

            onMainActivityListener.onFilterApplied(month, year)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }

    companion object {
        const val TAG: String = "FilterDialog"
        const val FILTER_CANCELLED = -1
        const val MIN_MONTH = 1
        const val MAX_MONTH = 12
        const val MIN_YEAR = 2020
        const val MAX_YEAR = 2100
    }
}