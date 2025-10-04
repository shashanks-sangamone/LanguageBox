package com.smvitm.languagebox.LBService

import com.smvitm.languagebox.LBEntity.Companies
import com.smvitm.languagebox.LBEntity.Plans
import com.smvitm.languagebox.LBEntity.UserLists
import com.smvitm.languagebox.LBEntity.Users
import com.smvitm.languagebox.LBRepo.CompaniesRepo
import com.smvitm.languagebox.LBRepo.UsersRepo
import com.smvitm.languagebox.dto.UsersDto
import org.apache.catalina.User
import org.springframework.stereotype.Service

@Service
class UsersService(private val usersRepo: UsersRepo,private val companiesRepo: CompaniesRepo) {

    fun getAllUsers(): Any? {
        return try {
            val data = usersRepo.findAll()
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun getUserById(id: Int): Any? {
        return try {
            val data = usersRepo.findById(id)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun getUserByEmail(email: String): Any? {
        return try {
            val data = usersRepo.getByEmail(email)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun addUser(users: UsersDto): Any? {
        return try {
            val comp = companiesRepo.findById(users.companyId).map {
                Companies(
                    id = it.id,
                    companyName =it.companyName,
                    countryId =  it.countryId
                )
            }.orElse(null)
            val usr = Users(
                userName = users.userName,
                userEmail = users.userEmail,
                date1 = users.date1,
                role = users.role,
                companyId = comp
            )
            val data = usersRepo.save(usr)
            data
        } catch (e: Exception) {
            e.message
        }
    }
    fun deleteUserById(id:Int): Any? {
        return try {
            val data = usersRepo.deleteById(id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }
}