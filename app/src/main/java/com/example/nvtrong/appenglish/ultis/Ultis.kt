package com.example.nvtrong.appenglish.ultis

import android.content.Context
import android.content.Context.MODE_PRIVATE

object Ultis {
    const val KEY_TITLE = "title"
    const val KEY_TIME = "time"
    const val KEY_TIME_LONG = "time_long"
    const val DATA_PREFERENCES = "my_data"
    const val KEY_REFERENCES_DIR = "dir"
    fun convertTime(durationInMillis: Int): String {
        val millis = durationInMillis % 1000
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24

        val time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis)
        return "Time: $time"
    }

    fun getDir(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(DATA_PREFERENCES, MODE_PRIVATE)
        return sharedPreferences.getString(KEY_REFERENCES_DIR, "")
    }

    fun setDir(context: Context, dir: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(DATA_PREFERENCES, MODE_PRIVATE)
        return sharedPreferences.edit().putString(KEY_REFERENCES_DIR, dir).commit()
    }
}