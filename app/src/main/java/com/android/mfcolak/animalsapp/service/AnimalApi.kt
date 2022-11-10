package com.android.mfcolak.animalsapp.service

import com.android.mfcolak.animalsapp.model.Animal
import io.reactivex.Single
import retrofit2.http.GET

interface AnimalApi {
    //  URL  ->     https://raw.githubusercontent.com/MFColak/JSONDataSet/main/data.json
    // BASE_URL -> https://raw.githubusercontent.com/
    // end_url  -> MFColak/JSONDataSet/main/data.json

    @GET("MFColak/JSONDataSet/main/data.json")
    fun getAnimal() :Single <List<Animal>>
}