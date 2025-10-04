package com.smvitm.languagebox.LBService


import com.smvitm.languagebox.LBRepo.CompaniesRepo
import com.smvitm.languagebox.LBRepo.UserListsRepo
import com.smvitm.languagebox.LBRepo.UsersRepo
import com.smvitm.languagebox.dto.UserListDto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class UserListsService(private val userListsRepo: UserListsRepo,private val usersRepo: UsersRepo,private val companiesRepo: CompaniesRepo) {
    fun getAllUserLists(): Any? {
        return try {
            val data = userListsRepo.findAll()
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun getUserListById(id: Int): Any? {
        return try {
            val data = userListsRepo.findById(id)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun getUserList(filename: String,id: Int): Any? {
        return try {

            val f1 = File(System.getProperty("user.dir"),"upload/$id")
            val f2 = File(f1,filename).readLines()

            f2
        } catch (e: Exception) {
            e.message
        }
    }

    fun addUserList(file: MultipartFile,userLists: UserListDto): Any? {
        return try {

            val f1 = File(System.getProperty("user.dir"),"upload/${userLists.userId}")

            if (!f1.exists()){
                f1.mkdirs()
            }

            val f2 = File(f1,file.originalFilename!!)
            file.transferTo(f2)

            val data = userListsRepo.addUserList(userLists.userId,userLists.listName,userLists.filename)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun deleteUserListById(id:Int): Any? {
        return try {
            val data = userListsRepo.deleteById(id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }
    fun updateListNameById(listName : String,id:Int): Any? {
        return try {
            val data = userListsRepo.updateListNameById(listName=listName,id = id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }
}