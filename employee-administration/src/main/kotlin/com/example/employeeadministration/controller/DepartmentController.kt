package com.example.employeeadministration.controller

import com.example.employeeadministration.model.dto.DepartmentDto
import com.example.employeeadministration.repositories.department.DepartmentRepositoryGlobal
import com.example.employeeadministration.services.DepartmentService
import org.apache.kafka.common.protocol.types.Field
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

const val departmentUrl = "department"

@RestController
class DepartmentController(
        val departmentService: DepartmentService,
        val departmentRepository: DepartmentRepositoryGlobal
) {

    @GetMapping(departmentUrl)
    fun getAllDepartments(): ResponseEntity<List<DepartmentDto>> {
        return ok(departmentRepository.getAllByDeletedFalse().map { departmentService.mapEntityToDto(it) })
    }

    @GetMapping("$departmentUrl/{id}")
    fun getDepartmentById(@PathVariable("id") id: String): ResponseEntity<DepartmentDto> {
        return ok(departmentRepository.getByIdAndDeletedFalse(id).map { departmentService.mapEntityToDto(it) }.orElseThrow())

    }

    @PostMapping(departmentUrl)
    fun createDepartment(@RequestBody departmentDto: DepartmentDto): ResponseEntity<DepartmentDto> {
        return ok(departmentService.createDepartment(departmentDto))
    }

    @PutMapping(departmentUrl)
    fun updateDepartment(@RequestBody departmentDto: DepartmentDto): ResponseEntity<DepartmentDto> {
        return ok(departmentService.updateDepartment(departmentDto))
    }

    @DeleteMapping("$departmentUrl/{id}")
    fun deleteDepartment(@PathVariable("id") id: String) {
        departmentService.deleteDepartment(id)

    }
}