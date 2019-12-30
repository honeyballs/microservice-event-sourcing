package com.example.employeeadministration.controller

import com.example.employeeadministration.model.dto.EmployeeDto
import com.example.employeeadministration.services.EmployeeService
import com.example.employeeadministration.repositories.EmployeeRepositoryGlobal
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

const val URL= "employee"

@RestController
class EmployeeController(
        val employeeRepository: EmployeeRepositoryGlobal,
        val employeeService: EmployeeService
) {

    @GetMapping(URL)
    fun getAllEmployees(): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDeletedFalse().map { employeeService.mapEntityToDto(it) })
    }

    @GetMapping("$URL/{department}")
    fun getAllOfDepartment(@PathVariable("department") department: String): ResponseEntity<List<EmployeeDto>> {
        return ok(employeeRepository.getAllByDepartment(department).map { employeeService.mapEntityToDto(it) })
    }

    @GetMapping("$URL/{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<EmployeeDto> {
        return ok(employeeRepository.getById(id).map { employeeService.mapEntityToDto(it) }.orElseThrow())
    }

    @PostMapping(URL)
    fun createEmployee(@RequestBody dto: EmployeeDto): ResponseEntity<EmployeeDto> {
        return ok(employeeService.createEmployee(dto))
    }

    @PutMapping(URL)
    fun updateEmployee(@RequestBody dto: EmployeeDto): ResponseEntity<EmployeeDto> {
        return ok(employeeService.updateEmployee(dto))
    }

    @DeleteMapping("$URL/{id}")
    fun deleteEmployee(@PathVariable("id") id: String) {
        employeeService.deleteEmployee(id)
    }

}