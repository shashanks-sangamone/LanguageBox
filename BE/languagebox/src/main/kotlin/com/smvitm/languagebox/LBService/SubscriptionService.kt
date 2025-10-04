package com.smvitm.languagebox.LBService

import com.smvitm.languagebox.LBEntity.Devices
import com.smvitm.languagebox.LBEntity.Plans
import com.smvitm.languagebox.LBEntity.Subscription
import com.smvitm.languagebox.LBRepo.DevicesRepo
import com.smvitm.languagebox.LBRepo.PlanRepo
import com.smvitm.languagebox.LBRepo.SubscriptionRepo
import com.smvitm.languagebox.dto.SubscriptionDto
import org.springframework.stereotype.Service

@Service
class SubscriptionService(private val subscriptionRepo: SubscriptionRepo,private val devicesRepo: DevicesRepo,private val planRepo: PlanRepo) {

    fun getAllSubscription(): Any? {
        return try {
            val data = subscriptionRepo.findAll()
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun getSubscriptionById(id: Int): Any? {
        return try {
            val data = subscriptionRepo.findById(id)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun deleteSubscriptionById(id: Int): Any? {
        return try {
            val data = subscriptionRepo.deleteById(id)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun addSubscription(subscription: SubscriptionDto): Any? {
        return try {
            val devs: Devices = devicesRepo.findById(subscription.deviceId).map { Devices(
                deviceId = it.deviceId,
                deviceName = it.deviceName,
                companyId = it.companyId,
                date1 = it.date1,
                location = it.location,
                languageId = it.languageId
            ) }.orElse(null)

            val plan : Plans = planRepo.findById(subscription.planId).map { Plans(
                id = it.id,
                planName = it.planName,
                price = it.price,
                description = it.description
            ) }.orElse(null)

            val subs = Subscription(
                subscriptionEnds = subscription.subscriptionEnds,
                subscriptionStart = subscription.subscriptionStart,
                status = subscription.status,
                transactionId = subscription.transactionId,
                deviceId = devs,
                planId = plan
            )
            val data = subscriptionRepo.save(subs)
            data
        } catch (e: Exception) {
            e.message
        }
    }
}