package com.example.worktimeadministration.repositories.worktime

import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.VACATION_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WORKTIME_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class UsedHoursRepositoryLocal(
        val factory: StreamsBuilderFactoryBean
): UsedHoursRepository {

    override fun getAll(): List<UsedEmployeeVacationHours> {
        val store = factory.kafkaStreams.store("$VACATION_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, UsedEmployeeVacationHours>())
        val iterator = store.all()
        val list = mutableListOf<UsedEmployeeVacationHours>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getByEmployeeIdAndDeletedFalse(employeeId: String): Optional<UsedEmployeeVacationHours> {
        return Optional.ofNullable(getAll().firstOrNull { !it.deleted && it.employee == employeeId })
    }


}