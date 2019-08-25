package com.platform.lynch.servo.model


data class User( val email: String, val password: String )

data class OktaUser(
        val grant_type: String,
        val scope: String,
        val username: String,
        val password: String
)