package com.platform.lynch.servo.model

import java.io.Serializable


data class Config (
        var client_id: String,
        var redirect_uri: String,
        var issuer_uri: String,
        var proxy: String
    )