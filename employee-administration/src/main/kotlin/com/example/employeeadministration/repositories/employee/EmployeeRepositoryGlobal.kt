package com.example.employeeadministration.repositories.employee

import com.example.employeeadministration.controller.RPC_URL
import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class EmployeeRepositoryGlobal(
        val factory: StreamsBuilderFactoryBean
) : EmployeeRepository {

    override fun getAll(): List<Employee> {
        return getGlobalList("/employee")
    }

    override fun getAllByDeletedFalse(): List<Employee> {
        return getGlobalList("/employee/not-deleted")
    }

    override fun getAllByDepartmentAndDeletedFalse(department: String): List<Employee> {
        return getGlobalList("/employee/department/$department")
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Employee> {
        return getGlobalInstance("/employee/$id", id)
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(affix: String): List<Employee> {
        val encodedAffix = affix.replace(" ", "%20")
        return factory.kafkaStreams.allMetadataForStore("$EMPLOYEE_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$encodedAffix"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<Employee>()::class.java)?.toList() ?: emptyList<Employee>()
                }
    }

    private fun getGlobalInstance(affix: String, key: String): Optional<Employee> {
        val encodedAffix = affix.replace(" ", "%20")
        val metadata = factory.kafkaStreams.metadataForKey("$EMPLOYEE_AGGREGATE-store", key, Serdes.String().serializer())
        val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$encodedAffix"
        println(url)
        val employee = RestTemplate().getForObject(url, Employee::class.java)
        if (employee != null) {
            return Optional.of(employee)
        }
        return Optional.empty<Employee>()
    }

}