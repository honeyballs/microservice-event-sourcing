package com.example.worktimeadministration.repositories.worktime

import com.example.worktimeadministration.controllers.RPC_URL
import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.VACATION_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WORKTIME_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class UsedHoursRepositoryGlobal(
        val factory: StreamsBuilderFactoryBean
): UsedHoursRepository {

    override fun getAll(): List<UsedEmployeeVacationHours> {
        return getGlobalList("/used")
    }

    override fun getByEmployeeIdAndDeletedFalse(employeeId: String): Optional<UsedEmployeeVacationHours> {
        return getGlobalInstance("/used/$employeeId")
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself if required, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(url: String): List<UsedEmployeeVacationHours> {
        return factory.kafkaStreams.allMetadataForStore("$VACATION_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<UsedEmployeeVacationHours>()::class.java)?.toList()
                            ?: emptyList<UsedEmployeeVacationHours>()
                }
    }

    private fun getGlobalInstance(url: String): Optional<UsedEmployeeVacationHours> {
        var result = Optional.empty<UsedEmployeeVacationHours>()
        val allMetadata = factory.kafkaStreams.allMetadataForStore("$VACATION_AGGREGATE-store")
        for (metadata in allMetadata) {
            val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
            println(url)
            val entry: UsedEmployeeVacationHours? = RestTemplate().getForObject(url, UsedEmployeeVacationHours::class.java)
            if (entry != null) {
                return Optional.of(entry)
            }
        }
        return result
    }

}