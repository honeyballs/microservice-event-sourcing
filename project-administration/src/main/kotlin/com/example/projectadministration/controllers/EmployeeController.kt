package com.example.projectadministration.controllers

import com.example.projectadministration.model.employee.Employee
import com.example.projectadministration.streams.EmployeeRepository
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

const val EMPLOYEE_URL = "employee"

@RestController
class EmployeeController(val employeeRepository: EmployeeRepository) {

    @GetMapping(EMPLOYEE_URL)
    fun getAllEmployees(): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAll())
    }

}