package com.example.worktimeadministration.repositories

import com.example.worktimeadministration.model.project.Project
import java.util.*

interface ProjectRepository {

    fun getAll(): List<Project>
    fun getAllByDeletedFalse(): List<Project>
    fun getById(id: String): Optional<Project>
    fun getAllByIsRunning(): List<Project>
    fun getAllOfEmployeeId(employeeId: String): List<Project>
    fun getByName(name: String): List<Project>

}