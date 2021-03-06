package com.example.worktimeadministration.repositories.project

import com.example.worktimeadministration.model.aggregates.project.Project
import java.util.*

interface ProjectRepository {

    fun getAll(): List<Project>
    fun getAllByDeletedFalse(): List<Project>
    fun getById(id: String): Optional<Project>
    fun getAllByIsRunning(): List<Project>
    fun getAllOfEmployeeId(employeeId: String): List<Project>
    fun getByName(name: String): List<Project>

}