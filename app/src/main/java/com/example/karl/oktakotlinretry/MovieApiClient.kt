package com.example.karl.oktakotlinretry

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MovieApiClient {

    @GET("api/menu") fun getMovies(@Header("Authorization") token:String): Observable<MovieEmbedded>
    @POST("api/menu") fun addMovie(@Body movie: Movie): Completable
    @DELETE("api/menu/{id}") fun deleteMovie(@Path("id") id: Int) : Completable
    @PUT("api/menu/{id}") fun updateMovie(@Path("id")id: Int, @Body movie: Movie) : Completable

    companion object {

        fun create(): MovieApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.0.18:8080/")
                    .build()

            return retrofit.create(MovieApiClient::class.java)
        }
    }
}
