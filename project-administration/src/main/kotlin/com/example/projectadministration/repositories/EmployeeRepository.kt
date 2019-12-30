package com.example.projectadministration.repositories

import com.example.projectadministration.model.employee.Employee
import java.util.*

interface EmployeeRepository {

    fun getAll(): List<Employee>
    fun getAllByDeletedFalse(): List<Employee>
    fun getAllByIdIn(ids: List<String>): List<Employee>
    fun getAllByDepartment(department: String): List<Employee>
    fun getById(id: String): Optional<Employee>

}