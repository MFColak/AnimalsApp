package com.android.mfcolak.animalsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity //ROOM
data class Animal(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val animalName: String,

    @ColumnInfo(name = "location")
    @SerializedName("location")
    val animalLocation: String,

    @ColumnInfo(name = "lifespan")
    @SerializedName("lifespan")
    val animalLifeSpan: String,

    @ColumnInfo(name = "image")
    @SerializedName("image")
    val animalImage: String
) {


    @PrimaryKey(autoGenerate = true) //Room
    var uuid: Int = 0
}

data class AnimalPalette(var color: Int)


data class SmsInfo(
    var to: String,
    val text: String,
    val imageUrl: String?
)