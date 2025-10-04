package com.smvitm.languagebox.LBRepo

import com.smvitm.languagebox.LBEntity.Companies
import jakarta.persistence.Transient
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CompaniesRepo : JpaRepository<Companies,Int>{
//    update companies set company_name="" where id=1;
//    update companies set country_id="" where id=1;


    @Modifying
    @Transactional
    @Query("update Companies c set c.companyName=:companyName where c.id=:id")
    fun updateCompanyNameById(@Param("companyName") companyName:String,@Param("id") id:Int)

    @Modifying
    @Transactional
    @Query("update Companies c set c.countryId=:countryId where c.id=:id")
    fun updateCountryIdById(@Param("countryId") countryId:String,@Param("id") id:Int)
}