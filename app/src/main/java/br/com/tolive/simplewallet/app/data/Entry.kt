package br.com.tolive.simplewallet.app.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val entryId: Int,
    @ColumnInfo(name = "value") val value: Double,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int
) : Parcelable {
    constructor(value: Double, type: Int, description: String, entryDate: Date) :
            this(0, value, type, description,
                entryDate.day, entryDate.month, entryDate.year) {
                this.entryDate = entryDate
            }

    var entryDate: Date
        get() { return Date(day, month, year) }
        set(value) {}

    class Date (val day: Int, val month: Int, val year: Int) {
        override fun toString(): String {
            return LocalDate.of(year, (month+1), day).toString()
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
        parcel.writeInt(entryId)
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

    companion object CREATOR : Parcelable.Creator<Entry> {
        const val TYPE_GAIN = 0
        const val TYPE_EXPENSE = 1

        override fun createFromParcel(parcel: Parcel): Entry {
            return Entry(parcel)
        }

        override fun newArray(size: Int): Array<Entry?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Entry:" +
                " [description]" + description +
                " [value]" + value.toString() +
                " [type]" + type +
                " [date]" + entryDate.toString()
    }
}