package com.smvitm.languagebox.LBRepo

import com.smvitm.languagebox.LBEntity.Subscription
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionRepo : JpaRepository<Subscription,Int> {
}