package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBEntity.Plans
import com.smvitm.languagebox.LBService.PlanService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("plans")
class PlanController(private val planService: PlanService) {

    @GetMapping("/getAll")
    fun getAll(): Any? {
        return planService.getAllPlans()
    }

    @GetMapping("/get/{id}")
    fun getPlansById(@PathVariable id:Int): Any? {
        return planService.getPlansById(id = id)
    }

    @PostMapping("/add")
    fun addPlans(@RequestBody plans: Plans): Any? {
        return planService.addPlans(plans = plans)
    }
}