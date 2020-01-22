package com.example.employeeadministration.repositories.position

import com.example.employeeadministration.controller.RPC_URL
import com.example.employeeadministration.model.aggregates.DEPARTMENT_AGGREGATE
import com.example.employeeadministration.model.aggregates.Department
import com.example.employeeadministration.model.aggregates.POSITION_AGGREGATE
import com.example.employeeadministration.model.aggregates.Position
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class PositionRepositoryGlobal(val factory: StreamsBuilderFactoryBean): PositionRepository {

    override fun getAll(): List<Position> {
        return getGlobalList("/position")
    }

    override fun getAllByDeletedFalse(): List<Position> {
        return getGlobalList("/position/not-deleted")
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Position> {
        return getGlobalInstanceByKey("/position/$id", id)
    }

    override fun getByTitleAndDeletedFalse(title: String): Optional<Position> {
        return getGlobalInstance("/position/title/$title")
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(url: String): List<Position> {
        return factory.kafkaStreams.allMetadataForStore("$POSITION_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<Position>()::class.java)?.toList() ?: emptyList<Position>()
                }
    }

    private fun getGlobalInstanceByKey(url: String, key: String): Optional<Position> {
        val metadata = factory.kafkaStreams.metadataForKey("$POSITION_AGGREGATE-store", key, Serdes.String().serializer())
        val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
        println(url)
        val position = RestTemplate().getForObject(url, Position::class.java)
        if (position != null) {
            return Optional.of(position)
        }
        return Optional.empty<Position>()
    }

    private fun getGlobalInstance(url: String): Optional<Position> {
        var result = Optional.empty<Position>()
        val allMetadata = factory.kafkaStreams.allMetadataForStore("$POSITION_AGGREGATE-store")
        for (metadata in allMetadata) {
            val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$url"
            println(url)
            val position: Position? = RestTemplate().getForObject(url, Position::class.java)
            if (position != null) {
                return Optional.of(position)
            }
        }
        return result
    }

}