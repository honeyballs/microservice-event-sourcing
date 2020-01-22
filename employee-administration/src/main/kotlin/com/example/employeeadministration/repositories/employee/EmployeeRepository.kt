package com.example.employeeadministration.repositories.employee

import com.example.employeeadministration.model.aggregates.Employee
import java.util.*

interface EmployeeRepository {

    fun getAll(): List<Employee>
    fun getAllByDeletedFalse(): List<Employee>
    fun getAllByDepartmentAndDeletedFalse(department: String): List<Employee>
    fun getByIdAndDeletedFalse(id: String): Optional<Employee>

}