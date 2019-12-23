package com.example.employeeadministration.controller

import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.streams.EmployeeRepositoryLocal
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

const val RPC_URL = "rpc"

/**
 * Provide a way to access this service instance's partitioned employee store.
 * Only relevant when running replicated.
 *
 * Wraps the repository methods. This way every repository action across multiple instances can be executed in parallel.
 */
@RestController
class KafkaRPCController(val employeeRepository: EmployeeRepositoryLocal) {

    @GetMapping("$RPC_URL/employee")
    fun getAllEmployees(): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAll())
    }

    @GetMapping("$RPC_URL/employee/{key}")
    fun getEmployeeWithKey(@PathVariable("key") key: String): ResponseEntity<Employee> {
        return ok(employeeRepository.getById(key).orElseThrow())
    }

    @GetMapping("$RPC_URL/employee/not-deleted")
    fun getAllByDeletedFalse(): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/employee/{department}")
    fun getAllOfDepartment(@PathVariable("department") department: String): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAllByDepartment(department))
    }

}