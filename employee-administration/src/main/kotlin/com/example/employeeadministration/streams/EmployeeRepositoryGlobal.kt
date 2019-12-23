package com.example.employeeadministration.streams

import com.example.employeeadministration.controller.RPC_URL
import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import org.apache.kafka.streams.state.StreamsMetadata
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@Service
class EmployeeRepositoryGlobal(
        val factory: StreamsBuilderFactoryBean,
        val localRepository: EmployeeRepositoryLocal
) : EmployeeRepository {

    override fun getAll(): List<Employee> {
        return getReplicaList("/employee")
    }

    override fun getAllByDeletedFalse(): List<Employee> {
        return getReplicaList("/employee/not-deleted")
    }

    override fun getAllByDepartment(department: String): List<Employee> {
        return getReplicaList("/employee/$department")
    }

    override fun getById(id: String): Optional<Employee> {
        return getReplicaInstance("/employee/$id")
    }

    private fun getReplicaList(url: String): List<Employee> {
        return factory.kafkaStreams.allMetadataForStore("$EMPLOYEE_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<Employee>()::class.java)?.toList() ?: emptyList<Employee>()
                }
    }

    private fun getReplicaInstance(url: String): Optional<Employee> {
        for (metadata in factory.kafkaStreams.allMetadataForStore("$EMPLOYEE_AGGREGATE-store")) {
            val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
            println(url)
            val employee = RestTemplate().getForObject(url, Employee::class.java)
            if (employee != null) {
                return Optional.of(employee)
            }
        }
        return Optional.empty<Employee>()
    }

}