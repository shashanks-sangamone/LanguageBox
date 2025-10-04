package com.smvitm.languagebox.dto

import com.smvitm.languagebox.LBEntity.Devices
import com.smvitm.languagebox.LBEntity.Plans
import java.time.LocalDateTime

data class SubscriptionDto (
    val subscriptionStart: LocalDateTime,
    val subscriptionEnds: LocalDateTime,
    val transactionId: String,
    val deviceId: Int,
    val status: Byte,
    val planId: Int
)