package com.platform.lynch.servo.model

import android.app.Activity
import com.platform.lynch.servo.activity.MainActivity
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

        fun create(activity: Activity): TicketApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl((activity as MainActivity).config!!.proxy.toString())
                    .build()

            return retrofit.create(TicketApiClient::class.java)
        }
    }
}
