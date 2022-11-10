package com.android.mfcolak.animalsapp.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.mfcolak.animalsapp.model.Animal

@Dao
interface AnimalDAO {   //DATA ACCESS OBJECT

    @Insert
    suspend fun insertAll(vararg animal: Animal): List<Long>

    //insert -> Room
    //suspend fun -> coroutine-> stop or continue
    // vararg -> multiple and as many objects as we want
    //List<Long> -> Long = uuid in Animal data class


    @Query("SELECT * FROM animal")
    suspend fun getAll() : List<Animal>

    @Query("SELECT * FROM animal WHERE uuid = :animalId")
    suspend fun getAnimal(animalId: Int): Animal

    @Query("DELETE FROM animal")
    suspend fun deleteAll()



}