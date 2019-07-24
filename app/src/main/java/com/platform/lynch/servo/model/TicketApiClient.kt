package com.platform.lynch.servo.model

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TicketApiClient {

    @GET("api/ticket") fun get(@Header("UserId") token: String): Observable<List<Ticket>>
    @POST("api/ticket") fun add(@Header("UserId") token: String, @Body ticket: Ticket): Completable

    companion object {

        fun create(): TicketApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.0.18:8080/")
                    .build()

            return retrofit.create(TicketApiClient::class.java)
        }
    }
}
