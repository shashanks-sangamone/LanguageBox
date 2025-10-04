package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBEntity.Companies
import com.smvitm.languagebox.LBService.CompaniesService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("companies")
class CompaniesController(private val companiesService: CompaniesService) {

    @GetMapping("/getAll")
    fun getAll(): Any? {
        return companiesService.getAllCompanies()
    }

    @GetMapping("/get/{id}")
    fun getAll(@PathVariable id:Int): Any? {
        return companiesService.getCompanyById(id= id)
    }

    @PostMapping("/add")
    fun addCompany(@RequestBody companies: Companies): Any? {
        return companiesService.addCompanies(companies = companies)
    }

    @PutMapping("/updateCompanyName/{companyName}/{id}")
    fun updateCompanyNameById(@PathVariable companyName:String,@PathVariable id:Int): Any? {
        return companiesService.updateCompanyNameById(id = id, companyName = companyName)
    }

    @PutMapping("/updateCountryId/{countryId}/{id}")
    fun updateCountryIdById(@PathVariable countryId:String,@PathVariable id:Int): Any? {
        return companiesService.updateCountryIdById(countryId = countryId,id = id)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteCompanyById(@PathVariable id:Int): Any? {
        return companiesService.deleteCompanyById(id=id)
    }
}