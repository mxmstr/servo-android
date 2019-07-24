package com.platform.lynch.servo.model

import java.util.*


data class Ticket(
        val id: Long,
        val customerId: String,
        val itemId: Long,
        val quantity: String,
        val options: String,
        val timestamp: String,
        val status: String
        )
