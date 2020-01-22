package com.example.worktimeadministration.controllers

import com.example.worktimeadministration.model.dto.WorktimeEntryDto
import com.example.worktimeadministration.repositories.worktime.WorktimeEntryRepositoryGlobal
import com.example.worktimeadministration.services.WorktimeEntryService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.time.Month

const val WORKTIME_URL = "worktime"

@RestController
class WorktimeEntryController(
        val worktimeEntryRepository: WorktimeEntryRepositoryGlobal,
        val worktimeEntryService: WorktimeEntryService
) {

    @GetMapping("$WORKTIME_URL/month/{month}")
    fun getAll(@PathVariable("month") month: Int): ResponseEntity<List<WorktimeEntryDto>> {
        return ok(worktimeEntryRepository.getAllOfMonth(Month.of(month)).map { worktimeEntryService.mapEntityToDto(it) })
    }

    @GetMapping("$WORKTIME_URL/{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<WorktimeEntryDto> {
        return ok(worktimeEntryRepository.getById(id).map { worktimeEntryService.mapEntityToDto(it) }.orElseThrow())
    }

    @GetMapping("$WORKTIME_URL/project/{projectId}")
    fun getByProject(@PathVariable("projectId") projectId: String): ResponseEntity<List<WorktimeEntryDto>> {
        return ok(worktimeEntryRepository.getOfProject(projectId).map { worktimeEntryService.mapEntityToDto(it) })
    }

    @GetMapping("$WORKTIME_URL/employee/{employeeId}")
    fun getByEmployee(@PathVariable("employeeId") employeeId: String): ResponseEntity<List<WorktimeEntryDto>> {
        return ok(worktimeEntryRepository.getOfEmployee(employeeId).map { worktimeEntryService.mapEntityToDto(it) })
    }

    @GetMapping("$WORKTIME_URL/employee/{employeeId}/{projectId}")
    fun getByEmployeeAndProject(@PathVariable("employeeId") employeeId: String, @PathVariable("projectId") projectId: String): ResponseEntity<List<WorktimeEntryDto>> {
        return ok(worktimeEntryRepository.getOfEmployeeAndProject(employeeId, projectId).map { worktimeEntryService.mapEntityToDto(it) })
    }

    @PostMapping(WORKTIME_URL)
    fun createEntry(@RequestBody worktimeEntryDto: WorktimeEntryDto): ResponseEntity<WorktimeEntryDto> {
        return ok(worktimeEntryService.createWorktimeEntry(worktimeEntryDto))
    }

    @PutMapping(WORKTIME_URL)
    fun updateEntry(@RequestBody worktimeEntryDto: WorktimeEntryDto): ResponseEntity<WorktimeEntryDto> {
        return ok(worktimeEntryService.updateEntry(worktimeEntryDto))
    }

    @DeleteMapping("$WORKTIME_URL/{id}")
    fun deleteEntry(@PathVariable("id") id: String) {
        worktimeEntryService.deleteEntry(id)
    }

}