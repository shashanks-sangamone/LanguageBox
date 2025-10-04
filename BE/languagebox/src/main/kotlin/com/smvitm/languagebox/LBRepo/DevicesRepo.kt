package com.smvitm.languagebox.LBRepo

import com.smvitm.languagebox.LBEntity.Devices
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DevicesRepo : JpaRepository<Devices,Int> {
    @Query("update Devices d set d.deviceName=:deviceName where d.deviceId=:deviceId")
    fun updateCountryIdById(@Param("deviceName") deviceName:String,@Param("deviceId") deviceId:Int)
}