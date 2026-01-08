package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBService.UserListsService
import com.smvitm.languagebox.dto.UserListDto
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("userlists")
class UserListsController(private val userListsService: UserListsService) {
    @GetMapping("/getAll")
    fun getAll(): Any? {
        return userListsService.getAllUserLists()
    }

    @GetMapping("/get/{id}")
    fun getUserById(@PathVariable id:Int): Any? {
        return userListsService.getUserListById(id = id)
    }

    @GetMapping("/getByUser/{userId}")
    fun getUserByUserId(@PathVariable userId:Int): Any? {
        return userListsService.getUserListByUserId(userId = userId)
    }

    @GetMapping("/read/{id}/{filename}")
    fun getUserList(@PathVariable filename:String,@PathVariable id:Int): Any? {
        return userListsService.getUserList(filename = filename,id=id)
    }

    @PostMapping("/add",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addUserList(@RequestPart file: MultipartFile, @RequestParam userId: Int,
                 @RequestParam listName:String,
                 @RequestParam filename : String): Any? {
        val userList = UserListDto(userId = userId, filename = filename, listName = listName)
        return userListsService.addUserList(file=file, userLists = userList)
    }

    @PostMapping("/update/byUserList",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateUserListById(@RequestPart file: MultipartFile, @RequestParam id: Int): Any? {
        return userListsService.updateUserListById(file = file, id = id)
    }

    @PostMapping("/update/listNameBy/{id}/{listName}",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateListNameById(@PathVariable id: Int, @PathVariable listName: String): Any? {
        return userListsService.updateListNameById(listName = listName,id = id)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteUserListById(@PathVariable id: Int): Any? {
        return userListsService.deleteUserListById(id = id)
    }

}