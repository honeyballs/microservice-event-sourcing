package com.example.projectadministration.controllers

import com.example.projectadministration.model.dto.ProjectDto
import com.example.projectadministration.repositories.ProjectRepositoryGlobal
import com.example.projectadministration.services.ProjectService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

const val PROJECT_URL = "project"

@RestController
class ProjectController(
        val projectService: ProjectService,
        val projectRepositoryGlobal: ProjectRepositoryGlobal
) {

    @GetMapping(PROJECT_URL)
    fun getAll(): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepositoryGlobal.getAllByDeletedFalse().map { projectService.mapEntityToDto(it) })
    }

    @GetMapping("$PROJECT_URL/{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<ProjectDto> {
        return ok(projectRepositoryGlobal.getById(id).map { projectService.mapEntityToDto(it) }.orElseThrow())
    }

    @GetMapping("$PROJECT_URL/running")
    fun getAllRunning(): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepositoryGlobal.getAllByIsRunning().map { projectService.mapEntityToDto(it) })
    }

    @GetMapping("$PROJECT_URL/name/{name}")
    fun getByName(@PathVariable("name") name: String): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepositoryGlobal.getByName(name).map { projectService.mapEntityToDto(it) })
    }

    @GetMapping("$PROJECT_URL/employee/{id}")
    fun getByEmployeeId(@PathVariable("id") id: String): ResponseEntity<List<ProjectDto>> {
        return ok(projectRepositoryGlobal.getAllOfEmployeeId(id).map { projectService.mapEntityToDto(it) })
    }

    @PostMapping(PROJECT_URL)
    fun createProject(@RequestBody projectDto: ProjectDto): ResponseEntity<ProjectDto> {
        return ok(projectService.createProject(projectDto))
    }

    @PutMapping(PROJECT_URL)
    fun updateProject(@RequestBody projectDto: ProjectDto): ResponseEntity<ProjectDto> {
        return ok(projectService.updateProject(projectDto))
    }

    @DeleteMapping("$PROJECT_URL/{id}")
    fun deleteProject(@PathVariable("id") id: String) {
        projectService.deleteProject(id)
    }
}