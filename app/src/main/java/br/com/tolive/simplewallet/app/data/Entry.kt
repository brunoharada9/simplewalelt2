package br.com.tolive.simplewallet.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "entries")
data class Entry (
    @PrimaryKey(autoGenerate = true) val entryId: Int,
    @ColumnInfo(name = "value") val value: Double?,
    @ColumnInfo(name = "type") val type: Int?,
    @ColumnInfo(name = "description") val description: String,
    val day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH),
    val year: Int = Calendar.getInstance().get(Calendar.YEAR)
){
    constructor(value: Double, type: Int, description: String) :
            this(0, value, type, description)

    companion object {
        const val TYPE_GAIN = 0
        const val TYPE_EXPENSE = 1
    }
}