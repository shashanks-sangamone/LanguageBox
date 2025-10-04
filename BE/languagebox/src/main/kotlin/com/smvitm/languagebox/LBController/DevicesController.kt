package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBEntity.Devices
import com.smvitm.languagebox.LBService.DevicesService
import com.smvitm.languagebox.dto.DevicesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("devices")
class DevicesController(private val devicesService: DevicesService) {

    @GetMapping("/getAll")
    fun getAll(): Any? {
        return devicesService.getAllDevice()
    }

    @GetMapping("/get/{id}")
    fun getDevicesById(@PathVariable id:Int): Any? {
        return devicesService.getDeviceById(id = id)
    }

    @PostMapping("/add")
    fun addDevices(@RequestBody devicesDto: DevicesDto): Any? {
        return devicesService.addDevices(devicesDto = devicesDto)
    }

    @PutMapping("/update/{deviceId}")
    fun updateDeviceNameByDeviceId(@PathVariable deviceId:Int,@RequestBody deviceName: Map<String,String>):Any?{
        return devicesService.updateDeviceNameByDeviceId(deviceId = deviceId, deviceName = deviceName["deviceName"].toString())
    }
}