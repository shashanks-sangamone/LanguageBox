package com.smvitm.languagebox.LBEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

/*
create table devices(
device_id integer primary key unique,
device_name varchar(20) not null,
company_id integer not null,
date1 date not null,
location integer not null,
language_id varchar(3) not null,
foreign key (company_id) references companies(id)
);
*/



@Entity
@Table(name = "devices")
data class Devices (

    @Id
    @Column(name = "device_id")
    val deviceId: Int,

    @Column(name = "device_name")
    val deviceName: String,

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    val companyId: Companies,

    @Column(name = "date1")
    val date1 : LocalDate = LocalDate.now(),

    @Column(name = "location")
    val location : Int,

    @Column(name = "language_id")
    val languageId: String
)
