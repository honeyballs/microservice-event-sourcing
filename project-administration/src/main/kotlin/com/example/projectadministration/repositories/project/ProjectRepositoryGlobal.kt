package com.example.projectadministration.repositories.project

import com.example.projectadministration.controllers.RPC_URL
import com.example.projectadministration.model.aggregates.PROJECT_AGGREGATE
import com.example.projectadministration.model.aggregates.Project
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class ProjectRepositoryGlobal(
        val factory: StreamsBuilderFactoryBean
): ProjectRepository {

    override fun getAll(): List<Project> {
        return getGlobalList("/project")
    }

    override fun getAllByDeletedFalse(): List<Project> {
        return getGlobalList("/project/not-deleted")
    }

    override fun getById(id: String): Optional<Project> {
        return getGlobalInstance("/project/$id", id)
    }

    override fun getAllByIsRunning(): List<Project> {
        return getGlobalList("/project/running")
    }

    override fun getAllOfEmployeeId(employeeId: String): List<Project> {
        return getGlobalList("/project/employee/$employeeId")
    }

    override fun getAllOfCustomer(customerId: String): List<Project> {
        return getGlobalList("/project/employee/$customerId")
    }

    override fun getByName(name: String): List<Project> {
        return getGlobalList("/project/name/$name")
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself if required, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(affix: String): List<Project> {
        val encodedAffix = affix.replace(" ", "%20")
        return factory.kafkaStreams.allMetadataForStore("$PROJECT_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$encodedAffix"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<Project>()::class.java)?.toList() ?: emptyList<Project>()
                }
    }

    private fun getGlobalInstance(affix: String, key: String): Optional<Project> {
        val encodedAffix = affix.replace(" ", "%20")
        val metadata = factory.kafkaStreams.metadataForKey("$PROJECT_AGGREGATE-store", key, Serdes.String().serializer())
        val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$encodedAffix"
        println(url)
        val project = RestTemplate().getForObject(url, Project::class.java)
        if (project != null) {
            return Optional.of(project)
        }
        return Optional.empty<Project>()
    }

}