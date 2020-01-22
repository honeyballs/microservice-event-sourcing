package com.example.employeeadministration.repositories.position

import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.aggregates.POSITION_AGGREGATE
import com.example.employeeadministration.model.aggregates.Position
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class PositionRepositoryLocal(val factory: StreamsBuilderFactoryBean): PositionRepository {

    override fun getAll(): List<Position> {
        val store = factory.kafkaStreams.store("$POSITION_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Position>())
        val iterator = store.all()
        val list = mutableListOf<Position>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getAllByDeletedFalse(): List<Position> {
        return getAll().filter { !it.deleted }
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Position> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.id == id })
    }

    override fun getByTitleAndDeletedFalse(title: String): Optional<Position> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.title == title })
    }
}