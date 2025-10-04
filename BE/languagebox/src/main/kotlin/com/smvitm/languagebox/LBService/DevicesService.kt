package com.smvitm.languagebox.LBService

import com.smvitm.languagebox.LBEntity.Companies
import com.smvitm.languagebox.LBEntity.Devices
import com.smvitm.languagebox.LBRepo.CompaniesRepo
import com.smvitm.languagebox.LBRepo.DevicesRepo
import com.smvitm.languagebox.dto.DevicesDto
import org.springframework.stereotype.Service

@Service
class DevicesService(private val devicesRepo: DevicesRepo,private val companiesRepo: CompaniesRepo) {

    fun getAllDevice(): Any? {
        return try {
            val data = devicesRepo.findAll()
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun getDeviceById(id:Int): Any? {
        return try {

            val data = devicesRepo.findById(id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun deleteDevicesById(id:Int): Any? {
        return try {
            val data = devicesRepo.deleteById(id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun addDevices(devicesDto: DevicesDto):Any?{
        return try{
            val comp = companiesRepo.findById(devicesDto.companyId).map {
                Companies(
                    id = it.id,
                    companyName =it.companyName,
                    countryId =  it.countryId
                )
            }.orElse(null)
            val devices = Devices(
             companyId = comp,
                deviceId = devicesDto.deviceId,
                deviceName = devicesDto.deviceName,
                date1 = devicesDto.date1,
                location = devicesDto.location,
                languageId = devicesDto.languageId
            )
            val data = devicesRepo.save(devices)
            data
        }
        catch (e:Exception){
            e.message
        }
    }

    fun updateDeviceNameByDeviceId(deviceId:Int,deviceName: String): Any?{
        return try{
            val data = devicesRepo.updateCountryIdById(deviceId = deviceId,deviceName = deviceName)
            data
        }
        catch (e:Exception){
            e.message
        }
    }
}