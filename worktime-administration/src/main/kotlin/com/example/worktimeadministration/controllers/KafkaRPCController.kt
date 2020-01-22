package com.example.worktimeadministration.controllers

import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.repositories.worktime.UsedHoursRepository
import com.example.worktimeadministration.repositories.worktime.UsedHoursRepositoryLocal
import com.example.worktimeadministration.repositories.worktime.WorktimeEntryRepositoryLocal
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.Month

const val RPC_URL = "rpc"

@RestController
class KafkaRPCController(
        val worktimeEntryRepository: WorktimeEntryRepositoryLocal,
        val usedHoursRepository: UsedHoursRepositoryLocal
) {

    @GetMapping("$RPC_URL/worktime")
    fun getAllEntries(): ResponseEntity<List<WorktimeEntry>> {
        return ok(worktimeEntryRepository.getAll())
    }

    @GetMapping("$RPC_URL/worktime/{key}")
    fun getEntryByKey(@PathVariable("key") key: String): ResponseEntity<WorktimeEntry> {
        return ok(worktimeEntryRepository.getById(key).orElse(null))
    }

    @GetMapping("$RPC_URL/worktime/not-deleted")
    fun getAllByDeletedFalse(): ResponseEntity<List<WorktimeEntry>> {
        return ok(worktimeEntryRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/worktime/employee/{employeeId}")
    fun getOfEmployee(@PathVariable("employeeId") employeeId: String): ResponseEntity<List<WorktimeEntry>> {
        return ok(worktimeEntryRepository.getOfEmployee(employeeId))
    }

    @GetMapping("$RPC_URL/worktime/project/{projectId}")
    fun getOfProject(@PathVariable("projectId") projectId: String): ResponseEntity<List<WorktimeEntry>> {
        return ok(worktimeEntryRepository.getOfProject(projectId))
    }

    @GetMapping("$RPC_URL/worktime/employee/{employeeId}/{projectId}")
    fun getOfEmployeeAndProject(@PathVariable("employeeId") employeeId: String, @PathVariable("projectId") projectId: String): ResponseEntity<List<WorktimeEntry>> {
        return ok(worktimeEntryRepository.getOfEmployeeAndProject(employeeId, projectId))
    }

    @GetMapping("$RPC_URL/worktime/month/{month}")
    fun getAllOfMonth(@PathVariable("month") month: Int): ResponseEntity<List<WorktimeEntry>> {
        return ok(worktimeEntryRepository.getAllOfMonth(Month.of(month)))
    }

    // USED HOURS ENDPOINT

    @GetMapping("$RPC_URL/used")
    fun getAllHoursEntries(): ResponseEntity<List<UsedEmployeeVacationHours>> {
        return ok(usedHoursRepository.getAll())
    }

    @GetMapping("$RPC_URL/used/{employee}")
    fun getHoursEntryByEmployeeKey(@PathVariable("employee") employee: String): ResponseEntity<UsedEmployeeVacationHours> {
        return ok(usedHoursRepository.getByEmployeeIdAndDeletedFalse(employee).orElse(null))
    }

}