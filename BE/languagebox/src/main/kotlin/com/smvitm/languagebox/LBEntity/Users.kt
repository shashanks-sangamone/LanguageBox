package com.smvitm.languagebox.LBEntity

import com.smvitm.languagebox.emums.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate


/*create table users(
id integer primary key auto_increment,
company_id integer not null,
user_name varchar(40) not null,
user_email varchar(40) not null unique,
date1 date not null,
role enum("USER","ADMIN") default "USER" not null,
foreign key (company_id) references companies(id)
);*/


@Entity
@Table(name = "users")
data class Users (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Int=0,

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    val companyId: Companies,

    @Column(name = "user_name")
    val userName: String,

    @Column(name = "user_email")
    val userEmail:String,

    @Column(name = "date1")
    val date1: LocalDate = LocalDate.now(),

    @Column(name="role")
    val role : Role = Role.USER
)