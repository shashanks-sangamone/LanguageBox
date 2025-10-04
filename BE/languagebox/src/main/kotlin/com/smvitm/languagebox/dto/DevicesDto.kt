package com.smvitm.languagebox.dto


import java.time.LocalDate

data class DevicesDto (
    val deviceId: Int,
    val deviceName: String,
    val companyId: Int,
    val date1 : LocalDate = LocalDate.now(),
    val location : Int,
    val languageId: String
)
