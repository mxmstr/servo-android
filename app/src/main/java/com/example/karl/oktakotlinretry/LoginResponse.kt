package com.example.karl.oktakotlinretry


data class UserInfo(val id: String)

data class Embedded(val user: UserInfo)

data class LoginResponse(val _embedded: Embedded)

