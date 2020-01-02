package com.example.worktimeadministration.repositories

import com.example.worktimeadministration.model.aggregates.WORKTIME_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.project.PROJECT_AGGREGATE
import com.example.worktimeadministration.model.project.Project
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.time.Month
import java.util.*

@Service
class WorktimeEntryRepositoryLocal(
        val factory: StreamsBuilderFactoryBean
) : WorktimeEntryRepository {

    override fun getAll(): List<WorktimeEntry> {
        val store = factory.kafkaStreams.store("$WORKTIME_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, WorktimeEntry>())
        val iterator = store.all()
        val list = mutableListOf<WorktimeEntry>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getAllByDeletedFalse(): List<WorktimeEntry> {
        return getAll().filter { !it.deleted }
    }

    override fun getById(id: String): Optional<WorktimeEntry> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.id == id })
    }

    override fun getOfEmployee(employeeId: String): List<WorktimeEntry> {
        return getAllByDeletedFalse().filter { it.employee == employeeId }
    }

    override fun getOfProject(projectId: String): List<WorktimeEntry> {
        return getAllByDeletedFalse().filter { it.project == projectId }
    }

    override fun getOfEmployeeAndProject(employeeId: String, projectId: String): List<WorktimeEntry> {
        return getOfEmployee(employeeId).filter { it.project == projectId }
    }

    override fun getAllOfMonth(month: Month): List<WorktimeEntry> {
        return getAllByDeletedFalse().filter { it.startTime.month == month && it.endTime.month == month }
    }
}