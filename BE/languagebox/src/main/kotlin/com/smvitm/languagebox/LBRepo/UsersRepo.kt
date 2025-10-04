package com.smvitm.languagebox.LBRepo

import com.smvitm.languagebox.LBEntity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UsersRepo : JpaRepository<Users,Int> {


    @Query("SELECT u FROM Users u where u.userEmail=:userEmail")
    fun getByEmail(@Param("userEmail") userEmail:String): Users
}