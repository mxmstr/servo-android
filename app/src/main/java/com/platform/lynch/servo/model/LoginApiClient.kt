package com.platform.lynch.servo.model

import android.app.Activity
import com.platform.lynch.servo.activity.MainActivity
import io.reactivex.Observable
import okhttp3.Credentials
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginApiClient {

    @POST("api/v1/authn") fun login(@Body user: OktaUser): Observable<LoginResponse>
    @FormUrlEncoded
    @POST("oauth2/default/v1/token")
    fun getToken(@Header("Authorization") credentials: String,
                 @Field("grant_type") grant_type: String, @Field("scope") scope: String,
                 @Field("username") username: String, @Field("password") password: String): Observable<TokenResponse>

    companion object {

        fun create(activity: Activity): LoginApiClient {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://dev-486832.okta.com/")
                    .build()

            return retrofit.create(LoginApiClient::class.java)
        }
    }
}
