package com.example.worktimeadministration.repositories.employee

import com.example.worktimeadministration.model.aggregates.employee.EMPLOYEE_AGGREGATE
import com.example.worktimeadministration.model.aggregates.employee.Employee
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeRepositoryImpl(
        val factory: StreamsBuilderFactoryBean
) : EmployeeRepository {

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

    override fun getById(id: String): Optional<Employee> {
        val employees = getAllByDeletedFalse()
        return Optional.ofNullable(employees.firstOrNull { it.id == id })
    }


}