package com.platform.lynch.servo.model

import java.io.Serializable


data class Config (
        var client_id: String = "0oap0ze7tuZcw1B5Z356",
        var client_secret: String = "9nGCV0VCsqTctnocYNiFMAdBab-rNF91Y88aX3se",
        var redirect_uri: String = "com.okta.dev-486832:/implicit/callback",
        var issuer_uri: String = "https://dev-486832.okta.com/oauth2/default",
        var proxy: String = "http://18.223.185.220:8080/"
    )