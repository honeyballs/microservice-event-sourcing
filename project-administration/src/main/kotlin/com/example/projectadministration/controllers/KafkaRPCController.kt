package com.example.projectadministration.controllers

import com.example.projectadministration.model.aggregates.Customer
import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.repositories.customer.CustomerRepository
import com.example.projectadministration.repositories.customer.CustomerRepositoryLocal
import com.example.projectadministration.repositories.project.ProjectRepositoryLocal
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

const val RPC_URL = "rpc"

@RestController
class KafkaRPCController(
        val customerRepository: CustomerRepositoryLocal,
        val projectRepository: ProjectRepositoryLocal
) {

    // CUSTOMER ENDPOINTS

    @GetMapping("$RPC_URL/customer")
    fun getAllCustomers(): ResponseEntity<List<Customer>> {
        return ok(customerRepository.getAll())
    }

    @GetMapping("$RPC_URL/customer/{key}")
    fun getCustomerByKey(@PathVariable("key") key: String): ResponseEntity<Customer> {
        return ok(customerRepository.getByIdAndDeletedFalse(key).orElse(null))
    }

    @GetMapping("$RPC_URL/customer/not-deleted")
    fun getAllCustomersByDeletedFalse(): ResponseEntity<List<Customer>> {
        return ok(customerRepository.getAllByDeletedFalse())
    }


    // PROJECT ENDPOINTS

    @GetMapping("$RPC_URL/project")
    fun getAllProjects(): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAll())
    }

    @GetMapping("$RPC_URL/project/{key}")
    fun getProjectByKey(@PathVariable("key") key: String): ResponseEntity<Project> {
        return ok(projectRepository.getById(key).orElse(null))
    }

    @GetMapping("$RPC_URL/project/not-deleted")
    fun getAllProjectsByDeletedFalse(): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/project/running")
    fun getAllRunningProjects(): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllByIsRunning())
    }

    @GetMapping("$RPC_URL/project/employee/{key}")
    fun getAllOfEmployee(@PathVariable("key") key: String): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllOfEmployeeId(key))
    }

    @GetMapping("$RPC_URL/project/customer/{key}")
    fun getAllOfCustomer(@PathVariable("key") key: String): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllOfCustomer(key))
    }

    @GetMapping("$RPC_URL/project/name/{name}")
    fun getByName(@PathVariable("name") name: String): ResponseEntity<List<Project>> {
        return ok(projectRepository.getByName(name))
    }

}