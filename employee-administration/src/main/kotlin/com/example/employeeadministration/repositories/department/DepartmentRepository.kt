package com.example.employeeadministration.repositories.department

import com.example.employeeadministration.model.aggregates.Department
import java.util.*

interface DepartmentRepository {

    fun getAll(): List<Department>
    fun getAllByDeletedFalse(): List<Department>
    fun getByIdAndDeletedFalse(id: String): Optional<Department>
    fun getByNameAndDeletedFalse(name: String): Optional<Department>

}