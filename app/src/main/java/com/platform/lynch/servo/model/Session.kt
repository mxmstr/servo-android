package com.platform.lynch.servo.model

import java.io.Serializable


class Session {
    companion object {
        var userId: String = ""
        var sessionToken: String = ""
        var seated: Boolean = false
        var table: Table = Table(0, "", "")
    }
}