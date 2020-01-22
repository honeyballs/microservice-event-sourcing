package com.example.employeeadministration.controller

import com.example.employeeadministration.model.dto.EmployeeDto
import com.example.employeeadministration.services.EmployeeService
import com.example.employeeadministration.repositories.employee.EmployeeRepositoryGlobal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

const val employeeUrl = "employee"

@RestController
class EmployeeController(
        val employeeRepository: EmployeeRepositoryGlobal,
        val employeeService: EmployeeService
) {

    @GetMapping(employeeUrl)
    fun getAllEmployees(): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDeletedFalse().map { employeeService.mapEntityToDto(it) })
    }

    @GetMapping("$employeeUrl/{id}")
    fun getEmployeeById(@PathVariable("id") id: String): ResponseEntity<EmployeeDto> {
        return ok(employeeRepository.getByIdAndDeletedFalse(id).map { employeeService.mapEntityToDto(it) }.orElseThrow())
    }

    @GetMapping("$employeeUrl/department/{depId}")
    fun getEmployeesOfDepartment(@PathVariable("depId") departmentId: String): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDepartmentAndDeletedFalse(departmentId).map { employeeService.mapEntityToDto(it) })
    }

    @PostMapping(employeeUrl)
    fun createEmployee(@RequestBody employeeDto: EmployeeDto): ResponseEntity<EmployeeDto> {
        return ok(employeeService.createEmployee(employeeDto))
    }

    @PutMapping(employeeUrl)
    fun updateEmployee(@RequestBody employeeDto: EmployeeDto): ResponseEntity<EmployeeDto> {
        return ok(employeeService.updateEmployee(employeeDto))
    }

    @DeleteMapping("$employeeUrl/{id}")
    fun deleteEmployee(@PathVariable("id") id: String) {
        employeeService.deleteEmployee(id)
    }

}