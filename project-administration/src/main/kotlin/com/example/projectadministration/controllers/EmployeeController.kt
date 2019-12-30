package com.example.projectadministration.controllers

import com.example.projectadministration.model.employee.Employee
import com.example.projectadministration.model.employee.EmployeeDto
import com.example.projectadministration.repositories.EmployeeRepositoryImpl
import com.example.projectadministration.services.EmployeeService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

const val EMPLOYEE_URL = "employee"

@RestController
class EmployeeController(
        val employeeRepository: EmployeeRepositoryImpl,
        val employeeService: EmployeeService
) {

    @GetMapping(EMPLOYEE_URL)
    fun getAllEmployees(): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDeletedFalse().map { employeeService.mapEntityToDto(it) })
    }

    @GetMapping("$EMPLOYEE_URL/{id}")
    fun getEmployeeById(@PathVariable("id") id: String): ResponseEntity<EmployeeDto> {
        return ok(employeeRepository.getById(id).map { employeeService.mapEntityToDto(it) }.orElseThrow())
    }

}