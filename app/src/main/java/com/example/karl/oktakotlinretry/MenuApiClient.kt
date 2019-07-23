package com.example.karl.oktakotlinretry

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MenuApiClient {

    @GET("api/menu") fun get(@Header("UserId") token: String): Observable<List<MenuItem>>
    @POST("api/menu") fun add(@Header("UserId") token: String, @Body item: MenuItem): Completable
    @DELETE("api/menu/{id}") fun delete(@Path("id") id: Long) : Completable
    @PUT("api/menu/{id}") fun update(@Path("id")id: Long, @Body item: MenuItem) : Completable

    companion object {

        fun create(): MenuApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.0.18:8080/")
                    .build()

            return retrofit.create(MenuApiClient::class.java)
        }
    }
}
