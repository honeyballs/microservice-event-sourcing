package com.example.employeeadministration.controller

import com.example.employeeadministration.model.aggregates.Department
import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.aggregates.Position
import com.example.employeeadministration.repositories.department.DepartmentRepository
import com.example.employeeadministration.repositories.department.DepartmentRepositoryLocal
import com.example.employeeadministration.repositories.employee.EmployeeRepositoryLocal
import com.example.employeeadministration.repositories.position.PositionRepository
import com.example.employeeadministration.repositories.position.PositionRepositoryLocal
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

const val RPC_URL = "rpc"

/**
 * Provide a way to access this service instance's partitioned employee store.
 * Only relevant when running replicated.
 *
 * Wraps the repository methods. This way every repository action across multiple instances can be executed in parallel.
 */
@RestController
class KafkaRPCController(
        val employeeRepository: EmployeeRepositoryLocal,
        val departmentRepository: DepartmentRepositoryLocal,
        val positionRepository: PositionRepositoryLocal
) {

    // EMPLOYEE ENDPOINTS

    @GetMapping("$RPC_URL/employee")
    fun getAllEmployees(): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAll())
    }

    @GetMapping("$RPC_URL/employee/{key}")
    fun getEmployeeWithKey(@PathVariable("key") key: String): ResponseEntity<Employee> {
        return ok(employeeRepository.getByIdAndDeletedFalse(key).orElse(null))
    }

    @GetMapping("$RPC_URL/employee/not-deleted")
    fun getAllByDeletedFalse(): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/employee/department/{department}")
    fun getAllOfDepartment(@PathVariable("department") department: String): ResponseEntity<List<Employee>> {
        return ok(employeeRepository.getAllByDepartmentAndDeletedFalse(department))
    }

    // DEPARTMENT ENDPOINTS

    @GetMapping("$RPC_URL/department")
    fun getAllDepartments(): ResponseEntity<List<Department>> {
        return ok(departmentRepository.getAll())
    }

    @GetMapping("$RPC_URL/department/not-deleted")
    fun getAllDepartmentsByDeletedFalse(): ResponseEntity<List<Department>> {
        return ok(departmentRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/department/{key}")
    fun getDepartmentWithKey(@PathVariable("key") key: String): ResponseEntity<Department> {
        return ok(departmentRepository.getByIdAndDeletedFalse(key).orElse(null))
    }

    @GetMapping("$RPC_URL/department/name/{name}")
    fun getDepartmentByName(@PathVariable("name") name: String): ResponseEntity<Department> {
        return ok(departmentRepository.getByNameAndDeletedFalse(name).orElse(null))
    }

    // POSITION ENDPOINTS

    @GetMapping("$RPC_URL/position")
    fun getAllPositions(): ResponseEntity<List<Position>> {
        return ok(positionRepository.getAll())
    }

    @GetMapping("$RPC_URL/position/not-deleted")
    fun getAllPositionsByDeletedFalse(): ResponseEntity<List<Position>> {
        return ok(positionRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/position/{key}")
    fun getPositionWithKey(@PathVariable("key") key: String): ResponseEntity<Position> {
        return ok(positionRepository.getByIdAndDeletedFalse(key).orElse(null))
    }

    @GetMapping("$RPC_URL/position/title/{title}")
    fun getPositionByTitle(@PathVariable("title") title: String): ResponseEntity<Position> {
        return ok(positionRepository.getByTitleAndDeletedFalse(title).orElse(null))
    }

}