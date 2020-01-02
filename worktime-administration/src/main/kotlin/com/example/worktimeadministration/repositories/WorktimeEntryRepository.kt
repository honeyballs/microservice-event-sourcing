package com.example.worktimeadministration.repositories

import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.employee.Employee
import java.time.Month
import java.util.*

interface WorktimeEntryRepository {

    fun getAll(): List<WorktimeEntry>
    fun getAllByDeletedFalse(): List<WorktimeEntry>
    fun getById(id: String): Optional<WorktimeEntry>
    fun getOfEmployee(employeeId: String): List<WorktimeEntry>
    fun getOfProject(projectId: String): List<WorktimeEntry>
    fun getOfEmployeeAndProject(employeeId: String, projectId: String): List<WorktimeEntry>
    fun getAllOfMonth(month: Month): List<WorktimeEntry>

}