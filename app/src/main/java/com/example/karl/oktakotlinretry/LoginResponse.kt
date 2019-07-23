package com.example.karl.oktakotlinretry

import com.google.gson.annotations.SerializedName


data class UserInfo(val id: String)

data class LoginData(val user: UserInfo)

data class LoginResponse(@SerializedName("_embedded" ) val data: LoginData)

