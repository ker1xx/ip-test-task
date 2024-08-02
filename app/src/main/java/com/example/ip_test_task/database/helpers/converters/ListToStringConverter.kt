package com.example.ip_test_task.database.helpers.converters

import android.util.Log
import androidx.room.TypeConverter

class ListToStringConverter {
    @TypeConverter
    fun fromListIntToString(stringList: List<String>): String = stringList.toString()

    @TypeConverter
    fun toListIntFromString(stringList: String): List<String> {
        val result = ArrayList<String>()
        val split = stringList.replace("[", "").replace("]", "").split(",")
        for (n in split) {
            try {
                result.add(n.toString())
            } catch (e: Exception) {
                Log.e("Error while converting list", e.message.toString())
            }
        }
        return result
    }
}