package com.platform.lynch.servo.model

import java.io.Serializable


data class Config (
        var client_id : String = "{CLIENT_ID}",
        var redirect_uri: String = "com.okta.dev-486832:/implicit/callback",
        var issuer_uri: String = "https://dev-486832.okta.com/oauth2/default",
        var proxy: String = "http://192.168.86.21:8080/"
    )