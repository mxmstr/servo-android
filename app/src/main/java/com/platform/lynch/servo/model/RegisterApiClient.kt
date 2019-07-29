package com.platform.lynch.servo.model

import android.app.Activity
import com.platform.lynch.servo.activity.LoginActivity
import com.platform.lynch.servo.activity.MainActivity
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RegisterApiClient {

    @POST("api/customer") fun createUser(@Body user: User): Observable<User>

    companion object {

        fun create(activity: Activity): RegisterApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl((activity as LoginActivity).config!!.proxy.toString())
                    .build()

            return retrofit.create(RegisterApiClient::class.java)
        }
    }
}
