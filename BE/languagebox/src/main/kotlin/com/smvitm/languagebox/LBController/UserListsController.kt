package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBService.UserListsService
import com.smvitm.languagebox.dto.UserListDto
import org.springframework.http.MediaType
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
}