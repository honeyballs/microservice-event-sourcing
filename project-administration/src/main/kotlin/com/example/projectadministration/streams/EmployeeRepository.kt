package com.example.projectadministration.streams

import com.example.projectadministration.model.employee.EMPLOYEE_AGGREGATE
import com.example.projectadministration.model.employee.Employee
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeRepository(val factory: StreamsBuilderFactoryBean) {

    fun getAll(): List<Employee> {
        val store = factory.kafkaStreams.store("$EMPLOYEE_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Employee>())
        val iterator = store.all()
        val list = mutableListOf<Employee>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    fun getAllByDeletedFalse(): List<Employee> {
        return getAll().filter { !it.deleted }
    }

    fun getEmployeeById(id: String): Optional<Employee> {
        val employees = getAllByDeletedFalse()
        return Optional.ofNullable(employees.firstOrNull { it.id == id })
    }

    fun getByIdIn(ids: List<String>): List<Employee> {
        return getAllByDeletedFalse().filter {
            ids.contains(it.id)
        }
    }

    fun getAllOfDepartment(department: String): List<Employee> {
        return getAllByDeletedFalse().filter { it.department == department }
    }

    fun getByName(nameString: String): List<Employee> {
        return getAllByDeletedFalse().filter {
            val string = "${it.firstname} ${it.lastname}"
            string.contains(nameString)
        }
    }

}