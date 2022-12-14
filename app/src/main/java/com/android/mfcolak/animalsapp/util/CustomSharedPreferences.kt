package com.android.mfcolak.animalsapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferences {

    companion object {
        private val TIME = "time"
        private var sharedPreferences: SharedPreferences? = null

        @Volatile
        private var instance: CustomSharedPreferences? = null

        private val lock = Any()
        operator fun invoke(context: Context): CustomSharedPreferences =
            instance ?: synchronized(lock) {

                instance ?: createCustomSharedPreferences(context).also {
                    instance = it
                }

            }

        private fun createCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }

    }

    fun saveCurrentTime(time: Long) {
        sharedPreferences?.edit(commit = true) {
            putLong(TIME, time)
        }

    }

    fun getCurrentTime() = sharedPreferences?.getLong(TIME, 0)

    fun getCacheSize() = sharedPreferences?.getString("cache_size", "")
}

