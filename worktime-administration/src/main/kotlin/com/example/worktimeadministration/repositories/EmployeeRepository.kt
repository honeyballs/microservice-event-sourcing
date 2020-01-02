package com.example.worktimeadministration.repositories

import com.example.worktimeadministration.model.employee.Employee
import java.util.*

interface EmployeeRepository {

    fun getAll(): List<Employee>
    fun getAllByDeletedFalse(): List<Employee>
    fun getById(id: String): Optional<Employee>

}