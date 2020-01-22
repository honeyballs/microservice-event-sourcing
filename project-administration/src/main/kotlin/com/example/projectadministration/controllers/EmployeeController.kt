package com.example.projectadministration.controllers

import com.example.projectadministration.model.dto.EmployeeDto
import com.example.projectadministration.repositories.employee.EmployeeRepositoryImpl
import com.example.projectadministration.services.EmployeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

const val employeeUrl = "employee"

@RestController
class EmployeeController(
        val employeeRepository: EmployeeRepositoryImpl,
        val employeeService: EmployeeService
) {

    @GetMapping(employeeUrl)
    fun getAllEmployees(): ResponseEntity<List<EmployeeDto>> {
        println("Employees requested")
        return ok(employeeRepository.getAllByDeletedFalse().map { employeeService.mapEntityToDto(it) })
    }

    @GetMapping("$employeeUrl/{id}")
    fun getEmployeeById(@PathVariable("id") id: String): ResponseEntity<EmployeeDto> {
        return ok(employeeRepository.getById(id).map { employeeService.mapEntityToDto(it) }.orElseThrow())
    }

    @GetMapping("$employeeUrl/department/{name}")
    fun getAllEmployeesInDepartment(@PathVariable("name") name: String): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDepartment(name).map { employeeService.mapEntityToDto(it) })
    }


}