package com.example.crudclass.api

import com.example.crudclass.model.Contact
import retrofit2.Call
import retrofit2.http.*

interface ContactCall {

    @GET("contacts")
    fun getAll(): Call<List<Contact>>

    @POST("contacts")
    fun save(@Body contact: Contact): Call<Contact>

    @DELETE("contacts/{id}")
    fun delete(@Path("id") id:Long): Call<String>


}