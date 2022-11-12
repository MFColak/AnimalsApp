package com.android.mfcolak.animalsapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.mfcolak.animalsapp.model.Animal

@Database(entities = [Animal::class], version = 1)
abstract class AnimalDatabase : RoomDatabase() {

    abstract fun animalDAO(): AnimalDAO

    //Singleton for sync
    companion object {

        @Volatile
        private var instance: AnimalDatabase? = null  //instance is visible for other threads

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AnimalDatabase::class.java, "animalDatabase"
        ).build()
    }

}


