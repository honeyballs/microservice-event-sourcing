package com.example.projectadministration.repositories.employee

import com.example.projectadministration.model.aggregates.employee.*
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeRepositoryImpl(
        val factory: StreamsBuilderFactoryBean,
        val departmentRepository: DepartmentRepositoryImpl
): EmployeeRepository {

    override fun getAll(): List<Employee> {
        val store = factory.kafkaStreams.store("$EMPLOYEE_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Employee>())
        val iterator = store.all()
        val list = mutableListOf<Employee>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getAllByDeletedFalse(): List<Employee> {
        return getAll().filter { !it.deleted }
    }

    override fun getAllByIdIn(ids: List<String>): List<Employee> {
        return getAllByDeletedFalse().filter { ids.contains(it.id) }
    }

    override fun getById(id: String): Optional<Employee> {
        val employees = getAllByDeletedFalse()
        return Optional.ofNullable(employees.firstOrNull { it.id == id })
    }

    override fun getAllByDepartment(name: String): List<Employee> {
        val department = departmentRepository.getByDepartmentName(name).orElseThrow()
        return getAllByDeletedFalse().filter { it.department == department.id }
    }

}

@Service
class DepartmentRepositoryImpl(
        val factory: StreamsBuilderFactoryBean
): DepartmentRepository {

    override fun getAll(): List<Department> {
        val store = factory.kafkaStreams.store("$DEPARTMENT_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Department>())
        val iterator = store.all()
        val list = mutableListOf<Department>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getByDepartmentId(id: String): Optional<Department> {
        return Optional.ofNullable(getAll().firstOrNull { it.id == id })
    }

    override fun getByDepartmentName(name: String): Optional<Department> {
        return Optional.ofNullable(getAll().firstOrNull {it.name == name})
    }

}

@Service
class PositionRepositoryImpl(
        val factory: StreamsBuilderFactoryBean
): PositionRepository {

    override fun getAll(): List<Position> {
        val store = factory.kafkaStreams.store("$POSITION_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Position>())
        val iterator = store.all()
        val list = mutableListOf<Position>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getByPositionId(id: String): Optional<Position> {
        return Optional.ofNullable(getAll().firstOrNull { it.id == id })
    }

}