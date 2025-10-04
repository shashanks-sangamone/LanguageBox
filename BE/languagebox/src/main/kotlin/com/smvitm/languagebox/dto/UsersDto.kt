package com.smvitm.languagebox.dto

import com.smvitm.languagebox.LBEntity.Companies
import com.smvitm.languagebox.emums.Role
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate

data class UsersDto(
    val companyId: Int,
    val userName: String,
    val userEmail:String,
    val date1: LocalDate = LocalDate.now(),
    val role : Role = Role.USER
)
