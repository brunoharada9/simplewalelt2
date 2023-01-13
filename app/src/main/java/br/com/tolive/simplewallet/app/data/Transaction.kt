package br.com.tolive.simplewallet.app.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int,
    @ColumnInfo(name = "value") var value: Double,
    @ColumnInfo(name = "type") var type: Int,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "day") var day: Int,
    @ColumnInfo(name = "month") var month: Int,
    @ColumnInfo(name = "year") var year: Int
) : Parcelable {
    constructor(value: Double, type: Int, description: String, transactionDate: Date) :
            this(
                0, value, type, description,
                transactionDate.day, transactionDate.month, transactionDate.year
            ) {
        this.transactionDate = transactionDate
    }

    var transactionDate: Date
        get() {
            return Date(day, month, year)
        }
        set(value) {}

    class Date(val day: Int, val month: Int, val year: Int) {
        override fun toString(): String {
            return LocalDate.of(year, (month + 1), day).toString()
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(transactionId)
        parcel.writeDouble(value)
        parcel.writeInt(type)
        parcel.writeString(description)
        parcel.writeInt(day)
        parcel.writeInt(month)
        parcel.writeInt(year)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        const val TYPE_GAIN = 0
        const val TYPE_EXPENSE = 1

        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Transaction:" +
                " [description]" + description +
                " [value]" + value.toString() +
                " [type]" + type +
                " [date]" + transactionDate.toString()
    }

    fun copyValues(tempTransaction: Transaction) {
        this.description = tempTransaction.description
        this.value = tempTransaction.value
        this.value = tempTransaction.value
        this.year = tempTransaction.year
        this.month = tempTransaction.month
        this.day = tempTransaction.day
    }
}