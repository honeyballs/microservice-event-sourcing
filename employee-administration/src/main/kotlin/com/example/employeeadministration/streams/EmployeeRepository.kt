package com.example.employeeadministration.streams

import com.example.employeeadministration.model.aggregates.Employee
import java.util.*

interface EmployeeRepository {

    fun getAll(): List<Employee>
    fun getAllByDeletedFalse(): List<Employee>
    fun getAllByDepartment(department: String): List<Employee>
    fun getById(id: String): Optional<Employee>

}