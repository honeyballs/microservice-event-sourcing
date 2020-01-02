package com.example.worktimeadministration.repositories

import com.example.worktimeadministration.controllers.RPC_URL
import com.example.worktimeadministration.model.aggregates.WORKTIME_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.Month
import java.util.*

@Service
class WorktimeEntryRepositoryGlobal(
        val factory: StreamsBuilderFactoryBean
) : WorktimeEntryRepository {

    override fun getAll(): List<WorktimeEntry> {
        return getGlobalList("/worktime")
    }

    override fun getAllByDeletedFalse(): List<WorktimeEntry> {
        return getGlobalList("/worktime/not-deleted")
    }

    override fun getById(id: String): Optional<WorktimeEntry> {
        return getGlobalInstance("/worktime/$id", id)
    }

    override fun getOfEmployee(employeeId: String): List<WorktimeEntry> {
        return getGlobalList("/worktime/employee/$employeeId")
    }

    override fun getOfProject(projectId: String): List<WorktimeEntry> {
        return getGlobalList("/worktime/project/$projectId")
    }

    override fun getOfEmployeeAndProject(employeeId: String, projectId: String): List<WorktimeEntry> {
        return getGlobalList("/worktime/employee/$employeeId/$projectId")
    }

    override fun getAllOfMonth(month: Month): List<WorktimeEntry> {
        return getGlobalList("/worktime/month/${month.value}")
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself if required, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(url: String): List<WorktimeEntry> {
        return factory.kafkaStreams.allMetadataForStore("$WORKTIME_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<WorktimeEntry>()::class.java)?.toList()
                            ?: emptyList<WorktimeEntry>()
                }
    }

    private fun getGlobalInstance(url: String, key: String): Optional<WorktimeEntry> {
        val metadata = factory.kafkaStreams.metadataForKey("$WORKTIME_AGGREGATE-store", key, Serdes.String().serializer())
        val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
        println(url)
        val entry = RestTemplate().getForObject(url, WorktimeEntry::class.java)
        if (entry != null) {
            return Optional.of(entry)
        }
        return Optional.empty<WorktimeEntry>()
    }
}