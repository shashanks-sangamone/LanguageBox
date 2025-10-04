package com.smvitm.languagebox.LBEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "subscription")
data class Subscription(
    /*create table subscription(
id integer auto_increment primary key,
subscription_start datetime not null,
subscription_ends datetime not null,
transaction_id varchar(40) unique not null,
device_id integer not null,
status tinyint not null,
plan_id integer not null unique,
foreign key (device_id) references devices(device_id),
foreign key (plan_id) references plans(id)
);
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Int = 0,

    @Column(name = "subscription_start")
    val subscriptionStart: LocalDateTime,

    @Column(name = "subscription_ends")
    val subscriptionEnds: LocalDateTime,

    @Column(name = "transaction_id")
    val transactionId: String,

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    val deviceId: Devices,

    @Column(name = "status")
    val status: Byte,

    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    val planId: Plans
)