package com.platform.lynch.servo.model

import com.google.gson.annotations.SerializedName


data class UserInfo(val id: String)

data class LoginData(val user: UserInfo)

data class LoginResponse(@SerializedName("_embedded" ) val data: LoginData, val sessionToken: String)

data class TokenResponse(val access_token: String)

