package com.example.employeeadministration.streams

import com.example.employeeadministration.model.aggregates.Aggregate
import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.events.Event
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.protocol.types.Field
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.GlobalKTable
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.stereotype.Component
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