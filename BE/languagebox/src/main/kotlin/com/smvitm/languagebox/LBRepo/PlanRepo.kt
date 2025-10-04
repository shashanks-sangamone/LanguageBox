package com.smvitm.languagebox.LBRepo

import com.smvitm.languagebox.LBEntity.Plans
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanRepo: JpaRepository<Plans,Int>  {

}