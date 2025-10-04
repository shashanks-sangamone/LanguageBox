package com.smvitm.languagebox.LBEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


/*
* create table user_lists(
id integer auto_increment primary key,
user_id integer not null,
list_name varchar(20) not null,
filename varchar(40) not null,
foreign key (user_id) references users(id)
);
*/

@Entity
@Table(name = "user_lists")
data class UserLists (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int,

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val userId: Users,

    @Column(name = "list_name")
    val listName:String,

    @Column(name = "filename")
    val filename : String
)