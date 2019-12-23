package com.example.employeeadministration.streams

import com.example.employeeadministration.controller.RPC_URL
import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.StreamsMetadata
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class EmployeeRepositoryLocal(val factory: StreamsBuilderFactoryBean) : EmployeeRepository {

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

    override fun getAllByDepartment(department: String): List<Employee> {
        return getAllByDeletedFalse().filter { it.department == department }
    }

    override fun getById(id: String): Optional<Employee> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.id == id })
    }

}