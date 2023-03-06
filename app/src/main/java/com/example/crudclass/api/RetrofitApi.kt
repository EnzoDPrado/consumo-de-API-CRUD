package com.example.crudclass.api

import com.example.crudclass.Constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {

    companion object{
        private lateinit var instance:Retrofit

        fun getRetrofit(): Retrofit{
            if(!::instance.isInitialized){
                instance = Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return instance
        }
    }



}