package com.example.employeeadministration.repositories.department

import com.example.employeeadministration.controller.RPC_URL
import com.example.employeeadministration.model.aggregates.DEPARTMENT_AGGREGATE
import com.example.employeeadministration.model.aggregates.Department
import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class DepartmentRepositoryGlobal(val factory: StreamsBuilderFactoryBean): DepartmentRepository {

    override fun getAll(): List<Department> {
        return getGlobalList("/department")
    }

    override fun getAllByDeletedFalse(): List<Department> {
        return getGlobalList("/department/not-deleted")
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Department> {
        return getGlobalInstanceByKey("/department/$id", id)
    }

    override fun getByNameAndDeletedFalse(name: String): Optional<Department> {
        return getGlobalInstance("/department/name/$name")
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(url: String): List<Department> {
        return factory.kafkaStreams.allMetadataForStore("$DEPARTMENT_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<Department>()::class.java)?.toList() ?: emptyList<Department>()
                }
    }

    private fun getGlobalInstanceByKey(url: String, key: String): Optional<Department> {
        val metadata = factory.kafkaStreams.metadataForKey("$DEPARTMENT_AGGREGATE-store", key, Serdes.String().serializer())
        val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
        println(url)
        val department = RestTemplate().getForObject(url, Department::class.java)
        if (department != null) {
            return Optional.of(department)
        }
        return Optional.empty<Department>()
    }

    private fun getGlobalInstance(url: String): Optional<Department> {
        var result = Optional.empty<Department>()
        val allMetadata = factory.kafkaStreams.allMetadataForStore("$DEPARTMENT_AGGREGATE-store")
        for (metadata in allMetadata) {
            val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
            println(url)
            val department: Department? = RestTemplate().getForObject(url, Department::class.java)
            if (department != null) {
                return Optional.of(department)
            }
        }
        return result
    }

}