package br.com.tolive.simplewallet.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "entries")
data class Entry (
    @PrimaryKey(autoGenerate = true) val entryId: Int,
    @ColumnInfo(name = "value") val value: Double,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int
){
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
            return LocalDate.of(year, month, day).toString()
        }
    }

    companion object {
        const val TYPE_GAIN = 0
        const val TYPE_EXPENSE = 1
    }
}