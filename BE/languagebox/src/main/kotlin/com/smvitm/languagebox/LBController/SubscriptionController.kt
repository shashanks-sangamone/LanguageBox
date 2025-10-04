package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBEntity.Subscription
import com.smvitm.languagebox.LBService.SubscriptionService
import com.smvitm.languagebox.dto.SubscriptionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("subscription")
class SubscriptionController(private val subscriptionService: SubscriptionService) {
    @GetMapping("/getAll")
    fun getAll(): Any? {
        return subscriptionService.getAllSubscription()
    }

    @GetMapping("/get/{id}")
    fun getPlansById(@PathVariable id:Int): Any? {
        return subscriptionService.getSubscriptionById(id = id)
    }

    @PostMapping("/add")
    fun addPlans(@RequestBody subscription: SubscriptionDto): Any? {
        return subscriptionService.addSubscription(subscription = subscription)
    }
}