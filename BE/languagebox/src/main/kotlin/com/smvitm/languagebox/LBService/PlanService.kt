package com.smvitm.languagebox.LBService

import com.smvitm.languagebox.LBEntity.Plans
import com.smvitm.languagebox.LBRepo.PlanRepo
import org.springframework.stereotype.Service

@Service
class PlanService(private val planRepo: PlanRepo) {

    fun getAllPlans(): Any? {
        return try {
            val data = planRepo.findAll()
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun getPlansById(id: Int): Any? {
        return try {
            val data = planRepo.findById(id)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun deletePlansById(id: Int): Any? {
        return try {
            val data = planRepo.deleteById(id)
            data
        } catch (e: Exception) {
            e.message
        }
    }

    fun addPlans(plans: Plans): Any? {
        return try {
            val data = planRepo.save(plans)
            data
        } catch (e: Exception) {
            e.message
        }
    }
}