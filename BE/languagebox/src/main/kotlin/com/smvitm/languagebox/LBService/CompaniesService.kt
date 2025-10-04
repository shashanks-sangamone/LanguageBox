package com.smvitm.languagebox.LBService

import com.smvitm.languagebox.LBEntity.Companies
import com.smvitm.languagebox.LBRepo.CompaniesRepo
import org.springframework.stereotype.Service

@Service
class CompaniesService(private val companiesRepo: CompaniesRepo) {
    fun getAllCompanies(): Any? {
        return try {
            val data = companiesRepo.findAll()
            data
        }
        catch (e:Exception){
            e.message
        }
    }

    fun getCompanyById(id:Int): Any? {
        return try {
            val data = companiesRepo.findById(id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun addCompanies(companies: Companies): Any? {
        return try {
            val data = companiesRepo.save<Companies>(companies)
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun updateCompanyNameById(companyName:String,id:Int): Any? {
        return try {
            val data = companiesRepo.updateCompanyNameById(companyName = companyName,id=id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun updateCountryIdById(countryId:String,id:Int): Any? {
        return try {
            val data = companiesRepo.updateCountryIdById(countryId = countryId,id=id)
            data
        }
        catch (e: Exception){
            e.message
        }
    }

    fun deleteCompanyById(id:Int): Any? {
        return try {
            val data = companiesRepo.deleteById(id)
            data
        } catch (e: Exception){
            e.message
        }
    }
}