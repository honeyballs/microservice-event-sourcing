package com.example.employeeadministration.repositories.department

import com.example.employeeadministration.model.aggregates.DEPARTMENT_AGGREGATE
import com.example.employeeadministration.model.aggregates.Department
import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class DepartmentRepositoryLocal(val factory: StreamsBuilderFactoryBean): DepartmentRepository {


    override fun getAll(): List<Department> {
        val store = factory.kafkaStreams.store("$DEPARTMENT_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Department>())
        val iterator = store.all()
        val list = mutableListOf<Department>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getAllByDeletedFalse(): List<Department> {
        return getAll().filter { !it.deleted }
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Department> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.id == id })
    }

    override fun getByNameAndDeletedFalse(name: String): Optional<Department> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.name == name })
    }


}