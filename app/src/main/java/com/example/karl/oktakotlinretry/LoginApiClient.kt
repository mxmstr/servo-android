package com.example.karl.oktakotlinretry

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginApiClient {

    @POST("api/v1/authn") fun getUser(@Body user: User): Observable<Response<LoginResponse>>

    companion object {

        fun create(): LoginApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://dev-486832.okta.com/")
                    .build()

            return retrofit.create(LoginApiClient::class.java)
        }
    }
}
