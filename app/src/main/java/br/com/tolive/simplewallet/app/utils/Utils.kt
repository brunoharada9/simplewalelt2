package br.com.tolive.simplewallet.app.utils

import android.content.res.Resources
import android.widget.EditText
import br.com.tolive.simplewallet.app.data.Transaction
import java.text.NumberFormat
import java.util.*

class Utils {

    companion object {

        fun convertEditTextToDouble(editTextValue: EditText, transactionType: Int): Double {
            var value = 0.0
            if (editTextValue.text.toString().isNotEmpty()) {
                value = editTextValue.text.toString().toDouble()
            }
            if (transactionType == Transaction.TYPE_EXPENSE) {
                value = -value
            }
            return value
        }

        fun getTransactionValueFormatted(transaction: Transaction): String? {
            return getValueFormatted(transaction.value)
        }

        fun getValueFormatted(value: Double): String? {
            val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.currency = Currency.getInstance(Locale.getDefault())

            return numberFormat.format(value)
        }

        fun dpToPx(dp: Int, resources: Resources): Int {
            return (dp * resources.displayMetrics.density).toInt()
        }
    }
}