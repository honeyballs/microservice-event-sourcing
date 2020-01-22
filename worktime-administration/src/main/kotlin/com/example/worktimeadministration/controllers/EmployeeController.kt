package com.example.worktimeadministration.controllers

import com.example.worktimeadministration.model.dto.EmployeeDto
import com.example.worktimeadministration.repositories.employee.EmployeeRepositoryImpl
import com.example.worktimeadministration.services.EmployeeService
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
    fun getAll(): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDeletedFalse().map { employeeService.mapEntityToDto(it) })
    }

    @GetMapping("$EMPLOYEE_URL/{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<EmployeeDto> {
        return ok(employeeRepository.getById(id).map { employeeService.mapEntityToDto(it) }.orElseThrow())
    }

}