package com.example.worktimeadministration.controllers

import com.example.worktimeadministration.model.employee.EmployeeDto
import com.example.worktimeadministration.model.project.ProjectDto
import com.example.worktimeadministration.repositories.ProjectRepositoryImpl
import com.example.worktimeadministration.services.ProjectService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

const val PROJECT_URL = "project"

@RestController
class ProjectController(
        val projectRepository: ProjectRepositoryImpl,
        val projectService: ProjectService
) {

    @GetMapping(PROJECT_URL)
    fun getAll(): ResponseEntity<List<ProjectDto>> {
        return ResponseEntity.ok(projectRepository.getAllByDeletedFalse().map { projectService.mapEntityToDto(it) })
    }

    @GetMapping("$PROJECT_URL/{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<ProjectDto> {
        return ResponseEntity.ok(projectRepository.getById(id).map { projectService.mapEntityToDto(it) }.orElseThrow())
    }

    @GetMapping("$PROJECT_URL/employee/{employeeId}")
    fun getByEmployee(@PathVariable("employeeId") employeeId: String): ResponseEntity<List<ProjectDto>> {
        return ResponseEntity.ok(projectRepository.getAllOfEmployeeId(employeeId).map { projectService.mapEntityToDto(it) })
    }

}