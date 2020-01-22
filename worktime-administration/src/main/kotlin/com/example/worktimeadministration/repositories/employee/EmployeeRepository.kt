package com.example.worktimeadministration.repositories.employee

import com.example.worktimeadministration.model.aggregates.employee.Employee
import java.util.*

interface EmployeeRepository {

    fun getAll(): List<Employee>
    fun getAllByDeletedFalse(): List<Employee>
    fun getById(id: String): Optional<Employee>

}