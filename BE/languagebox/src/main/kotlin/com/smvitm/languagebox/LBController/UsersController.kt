package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBService.UsersService
import com.smvitm.languagebox.dto.UsersDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UsersController(private val usersService: UsersService) {
    @GetMapping("/getAll")
    fun getAll(): Any? {
        return usersService.getAllUsers()
    }

    @GetMapping("/getById/{id}")
    fun getById(@PathVariable id:Int): Any? {
        return usersService.getUserById(id= id)
    }

    @GetMapping("/getByEmail/{email}")
    fun getByEmail(@PathVariable email:String): Any? {
        return usersService.getUserByEmail(email = email)
    }

    @PostMapping("/add")
    fun addCompany(@RequestBody users: UsersDto): Any? {
        return usersService.addUser(users = users)
    }
}