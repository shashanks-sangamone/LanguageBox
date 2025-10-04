package com.smvitm.languagebox.LBRepo

import com.smvitm.languagebox.LBEntity.UserLists
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserListsRepo : JpaRepository<UserLists,Int> {
    //    update user_lists set list_name="" where id=1;

    @Query("update UserLists u set u.listName=:listName where u.id=:id")
    fun updateListNameById(@Param("listName") listName:String, @Param("id") id:Int)

    @Modifying
    @Transactional
    @Query(
        value = "insert into user_lists(user_id,list_name,filename) values (:userId, :listName, :filename)",
        nativeQuery = true)
    fun addUserList(
        @Param("userId") userId: Int,
        @Param("listName") listName: String,
        @Param("filename") filename: String
    ): Int
}