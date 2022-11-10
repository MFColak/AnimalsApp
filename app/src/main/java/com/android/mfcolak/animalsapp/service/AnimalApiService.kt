package com.android.mfcolak.animalsapp.service

import com.android.mfcolak.animalsapp.model.Animal
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class AnimalApiService {
    //  URL  ->     https://raw.githubusercontent.com/MFColak/JSONDataSet/main/data.json
    // BASE_URL -> https://raw.githubusercontent.com/

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(AnimalApi::class.java)

    fun getData(): Single<List<Animal>>{

        return api.getAnimal()
    }
}