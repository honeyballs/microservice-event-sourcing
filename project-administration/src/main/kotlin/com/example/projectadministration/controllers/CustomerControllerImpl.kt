package com.example.projectadministration.controllers

import com.example.projectadministration.model.dto.CustomerDto
import com.example.projectadministration.repositories.customer.CustomerRepositoryGlobal
import com.example.projectadministration.repositories.project.ProjectRepositoryGlobal
import com.example.projectadministration.services.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

const val customerUrl = "customer"

@RestController
class CustomerControllerImpl(
        val customerRepository: CustomerRepositoryGlobal,
        val customerService: CustomerService,
        val projectRepository: ProjectRepositoryGlobal
) {

    @GetMapping(customerUrl)
    fun getAllCustomers(): ResponseEntity<List<CustomerDto>> {
        return ok(customerRepository.getAllByDeletedFalse().map { customerService.mapEntityToDto(it) })
    }

    @GetMapping("$customerUrl/{id}")
    fun getCustomerById(@PathVariable("id") id: String): ResponseEntity<CustomerDto> {
        return ok(customerRepository.getByIdAndDeletedFalse(id).map { customerService.mapEntityToDto(it) }.orElseThrow())
    }

    @PostMapping(customerUrl)
    fun createCustomer(@RequestBody customerDto: CustomerDto): ResponseEntity<CustomerDto> {
        return ok(customerService.createCustomer(customerDto))
    }

    @PutMapping(customerUrl)
    fun updateCustomer(customerDto: CustomerDto): ResponseEntity<CustomerDto> {
        return ok(customerService.updateCustomer(customerDto))
    }

    @DeleteMapping("$customerUrl/{id}")
    fun deleteCustomer(@PathVariable("id") id: String) {
        customerService.deleteCustomer(id)
    }
}