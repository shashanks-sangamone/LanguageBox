package com.smvitm.languagebox.LBEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


/*create table companies(
id integer primary key auto_increment,
company_name varchar(100) unique not null,
country_id varchar(3) not null
);*/

@Entity
@Table(name = "companies")
data class Companies(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int,

    @Column(name = "company_name")
    val companyName : String,

    @Column(name = "country_id")
    val countryId : String
)
