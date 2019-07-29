package com.platform.lynch.servo.model

import android.app.Activity
import android.content.Context
import com.platform.lynch.servo.R
import com.platform.lynch.servo.activity.MainActivity
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

        fun create(activity: Activity): MenuApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl((activity as MainActivity).config!!.proxy.toString())
                    .build()

            return retrofit.create(MenuApiClient::class.java)
        }
    }
}
