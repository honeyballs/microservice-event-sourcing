package com.example.worktimeadministration.repositories

import com.example.worktimeadministration.model.project.PROJECT_AGGREGATE
import com.example.worktimeadministration.model.project.Project
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectRepositoryImpl(val factory: StreamsBuilderFactoryBean): ProjectRepository {

    override fun getAll(): List<Project> {
        val store = factory.kafkaStreams.store("$PROJECT_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Project>())
        val iterator = store.all()
        val list = mutableListOf<Project>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getAllByDeletedFalse(): List<Project> {
        return getAll().filter { !it.deleted }
    }

    override fun getById(id: String): Optional<Project> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.id == id })
    }

    override fun getAllByIsRunning(): List<Project> {
        return getAllByDeletedFalse().filter { it.endDate == null }
    }

    override fun getAllOfEmployeeId(employeeId: String): List<Project> {
        return getAllByDeletedFalse().filter { it.employees.contains(employeeId) }
    }

    override fun getByName(name: String): List<Project> {
        return getAllByDeletedFalse().filter { it.name.contains(name) }
    }

}