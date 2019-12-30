package com.example.projectadministration.configuration


import com.example.projectadministration.model.aggregates.PROJECT_AGGREGATE
import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.model.employee.EMPLOYEE_AGGREGATE
import com.example.projectadministration.model.employee.Employee
import com.example.projectadministration.model.events.Event
import com.example.projectadministration.model.events.project.handleProjectEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.processor.WallclockTimestampExtractor
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.net.InetAddress

@Configuration
@EnableKafkaStreams
class KafkaStreamsConfig(final val mapper: ObjectMapper) {

    @Autowired
    lateinit var env: Environment

    lateinit var eventSerde: Serde<Event>
    lateinit var employeeSerde: Serde<Employee>
    lateinit var projectSerde: Serde<Project>

    init {
        val eventDeserializer = JsonDeserializer(Event::class.java, mapper)
        eventDeserializer.setUseTypeHeaders(false)
        val employeeDeserializer = JsonDeserializer(Employee::class.java, mapper)
        employeeDeserializer.setUseTypeHeaders(false)
        val projectDeserializer = JsonDeserializer(Project::class.java, mapper)
        eventSerde = Serdes.serdeFrom(JsonSerializer<Event>(mapper), eventDeserializer)
        employeeSerde = Serdes.serdeFrom(JsonSerializer<Employee>(mapper), employeeDeserializer)
        projectSerde = Serdes.serdeFrom(JsonSerializer<Project>(mapper), projectDeserializer)
    }

    @Bean
    fun projectStoreTopic(): NewTopic {
        return NewTopic("$PROJECT_AGGREGATE-table", 2, 1)
    }

    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun streamsConfiguration(): KafkaStreamsConfiguration {
        // Get the hostname of this service to put as application server
        // Setting this field allows us to query the complete application state of multiple instances via kafka
        val hostname = InetAddress.getLocalHost().hostName

        val configs = mutableMapOf<String, Any>()
        configs[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = env.getProperty("KAFKA_URL", "localhost:9093")
        configs[StreamsConfig.APPLICATION_ID_CONFIG] = "project-administration"
        configs[StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG] = WallclockTimestampExtractor::class.java.name
        configs[StreamsConfig.PROCESSING_GUARANTEE_CONFIG] = StreamsConfig.EXACTLY_ONCE
        configs[StreamsConfig.APPLICATION_SERVER_CONFIG] = "$hostname:8081"
        return KafkaStreamsConfiguration(configs)
    }

    @Bean
    fun createTableTopicFromStream(builder: StreamsBuilder): KStream<String, Project?> {
        return builder.stream<String, Event>(PROJECT_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { key: String, value: Event, aggregate: Project? -> handleProjectEvent(value, aggregate) },
                        Materialized.`as`<String, Project, KeyValueStore<Bytes, ByteArray>>("$PROJECT_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(projectSerde)
                )
                .toStream()
                .peek(ForeachAction { key: String , value: Project  -> println(value.toString()) })
                .through("$PROJECT_AGGREGATE-table", Produced.with(Serdes.String(), projectSerde))
    }

    @Bean
    fun createEmployeeStore(builder: StreamsBuilder): GlobalKTable<String, Employee> {
        return builder.globalTable(
                "$EMPLOYEE_AGGREGATE-table",
                Consumed.with(Serdes.String(), employeeSerde),
                Materialized.`as`("$EMPLOYEE_AGGREGATE-store")
        )
    }

}