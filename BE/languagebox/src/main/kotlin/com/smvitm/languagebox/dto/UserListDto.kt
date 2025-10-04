package com.smvitm.languagebox.dto

import com.smvitm.languagebox.LBEntity.Users
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

data class UserListDto(
    val userId: Int,
    val listName:String,
    val filename : String
)
