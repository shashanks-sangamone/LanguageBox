package com.smvitm.languagebox.LBEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/*
create table plans(
id integer auto_increment primary key,
plane_name varchar(20) not null unique,
price varchar(5) not null unique,
description text not null
);*/

@Entity
@Table(name = "plans")
data class Plans(

    /*
    * id integer auto_increment primary key,
    plane_name varchar(20) not null unique,
    price varchar(5) not null unique,
    discription text not null
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "plan_name")
    val planName: String,

    @Column(name = "price")
    val price : Int,

    @Column(name = "description")
    val description: String
)