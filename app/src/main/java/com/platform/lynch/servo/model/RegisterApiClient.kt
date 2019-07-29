package com.platform.lynch.servo.model

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RegisterApiClient {

    @POST("api/customer") fun createUser(@Body user: User): Observable<User>

    companion object {

        fun create(): RegisterApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.0.18:8080/")
                    .build()

            return retrofit.create(RegisterApiClient::class.java)
        }
    }
}
