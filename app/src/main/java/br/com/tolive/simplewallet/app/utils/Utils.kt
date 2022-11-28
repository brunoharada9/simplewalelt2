package br.com.tolive.simplewallet.app.utils

import android.widget.EditText
import br.com.tolive.simplewallet.app.data.Entry
import java.text.NumberFormat
import java.util.*

class Utils {

    companion object {

        fun convertEditTextToDouble(editTextValue: EditText, entryType: Int): Double {
            var value = 0.0
            if (editTextValue.text.toString().isNotEmpty()) {
                value = editTextValue.text.toString().toDouble()
            }
            if (entryType == Entry.TYPE_EXPENSE) {
                value = -value
            }
            return value
        }

        fun getEntryValueFormatted(current: Entry): String? {
            val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.currency = Currency.getInstance("USD")

            return numberFormat.format(current.value)
        }

        fun getValueFormatted(value: Double): String? {
            val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.currency = Currency.getInstance("USD")

            return numberFormat.format(value)
        }
    }
}