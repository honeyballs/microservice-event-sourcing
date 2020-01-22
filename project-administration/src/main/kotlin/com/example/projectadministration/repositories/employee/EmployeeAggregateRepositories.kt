package com.example.projectadministration.repositories.employee

import com.example.projectadministration.model.aggregates.employee.Department
import com.example.projectadministration.model.aggregates.employee.Employee
import com.example.projectadministration.model.aggregates.employee.Position
import org.springframework.stereotype.Repository
import java.util.*

interface EmployeeRepository {

    fun getAll(): List<Employee>
    fun getAllByDeletedFalse(): List<Employee>
    fun getAllByIdIn(ids: List<String>): List<Employee>
    fun getAllByDepartment(department: String): List<Employee>
    fun getById(id: String): Optional<Employee>

}

interface PositionRepository {

    fun getAll(): List<Position>
    fun getByPositionId(id: String): Optional<Position>

}

interface DepartmentRepository {

    fun getAll(): List<Department>
    fun getByDepartmentId(id: String): Optional<Department>
    fun getByDepartmentName(name: String): Optional<Department>

}