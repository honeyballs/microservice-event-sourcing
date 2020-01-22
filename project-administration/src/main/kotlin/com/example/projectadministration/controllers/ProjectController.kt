package com.example.projectadministration.controllers

import com.example.projectadministration.model.dto.ProjectDto
import com.example.projectadministration.repositories.project.ProjectRepositoryGlobal
import com.example.projectadministration.services.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

const val projectUrl = "project"

@RestController
class ProjectController(
        val projectService: ProjectService,
        val projectRepository: ProjectRepositoryGlobal
) {

    @GetMapping(projectUrl)
    fun getAllProjects(): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepository.getAllByDeletedFalse().map { projectService.mapEntityToDto(it) })
    }

    @GetMapping("$projectUrl/{id}")
    fun getProjectById(@PathVariable("id") id: String): ResponseEntity<ProjectDto> {
        return ok(projectRepository.getById(id).map { projectService.mapEntityToDto(it) }.orElseThrow())
    }

    @GetMapping("$projectUrl/customer/{id}")
    fun getProjectsOfCustomer(@PathVariable("id") customerId: String): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepository.getAllOfCustomer(customerId).map { projectService.mapEntityToDto(it) })
    }

    @GetMapping("$projectUrl/employee/{id}")
    fun getProjectsOfEmployee(@PathVariable("id") employeeId: String): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepository.getAllOfEmployeeId(employeeId).map { projectService.mapEntityToDto(it) })
    }

    @PostMapping(projectUrl)
    fun createProject(@RequestBody projectDto: ProjectDto): ResponseEntity<ProjectDto> {
        return ok(projectService.createProject(projectDto))
    }

    @PutMapping(projectUrl)
    fun updateProject(@RequestBody projectDto: ProjectDto): ResponseEntity<ProjectDto> {
        return ok(projectService.updateProject(projectDto))
    }

    @PutMapping("$projectUrl/finish/{id}")
    fun finishProject(@RequestBody projectDto: ProjectDto): ResponseEntity<ProjectDto> {
        return ok(projectService.finishProject(projectDto))
    }

    @DeleteMapping("$projectUrl/{id}")
    fun deleteProject(@PathVariable("id") id: String) {
            projectService.deleteProject(id)
    }
}