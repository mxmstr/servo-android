package com.platform.lynch.servo.model

import android.app.Activity
import android.util.Log
import com.platform.lynch.servo.activity.MainActivity
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TableApiClient {

    @GET("api/table") fun get(@Header("UserId") token: String): Observable<List<Table>>
    @GET("api/table/{id}") fun getById(@Path("id")id: Long) : Observable<Table>
    @PUT("api/table/{id}") fun update(@Path("id")id: Long, @Body table: Table) : Observable<Response<Any>>

    companion object {

        fun create(activity: Activity): TableApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl((activity as MainActivity).config!!.proxy.toString())
                    .build()

            return retrofit.create(TableApiClient::class.java)
        }
    }
}
