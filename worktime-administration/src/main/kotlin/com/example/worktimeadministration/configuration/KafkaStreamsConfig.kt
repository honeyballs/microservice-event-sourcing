package com.example.worktimeadministration.configuration


import com.example.worktimeadministration.model.aggregates.WORKTIME_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.employee.EMPLOYEE_AGGREGATE
import com.example.worktimeadministration.model.employee.Employee
import com.example.worktimeadministration.model.events.Event
import com.example.worktimeadministration.model.events.worktime.handleWorktimeEvent
import com.example.worktimeadministration.model.project.PROJECT_AGGREGATE
import com.example.worktimeadministration.model.project.Project
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
    lateinit var worktimeSerde: Serde<WorktimeEntry>

    init {
        val eventDeserializer = JsonDeserializer(Event::class.java, mapper)
        eventDeserializer.setUseTypeHeaders(false)
        val employeeDeserializer = JsonDeserializer(Employee::class.java, mapper)
        employeeDeserializer.setUseTypeHeaders(false)
        val projectDeserializer = JsonDeserializer(Project::class.java, mapper)
        projectDeserializer.setUseTypeHeaders(false)
        val worktimeDeserializer = JsonDeserializer(WorktimeEntry::class.java, mapper)
        worktimeDeserializer.setUseTypeHeaders(false)
        eventSerde = Serdes.serdeFrom(JsonSerializer<Event>(mapper), eventDeserializer)
        employeeSerde = Serdes.serdeFrom(JsonSerializer<Employee>(mapper), employeeDeserializer)
        projectSerde = Serdes.serdeFrom(JsonSerializer<Project>(mapper), projectDeserializer)
        worktimeSerde = Serdes.serdeFrom(JsonSerializer<WorktimeEntry>(mapper), worktimeDeserializer)
    }

    @Bean
    fun projectStoreTopic(): NewTopic {
        return NewTopic("$WORKTIME_AGGREGATE-table", 2, 1)
    }

    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun streamsConfiguration(): KafkaStreamsConfiguration {
        // Get the hostname of this service to put as application server
        // Setting this field allows us to query the complete application state of multiple instances via kafka
        val hostname = InetAddress.getLocalHost().hostName

        val configs = mutableMapOf<String, Any>()
        configs[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = env.getProperty("KAFKA_URL", "localhost:9093")
        configs[StreamsConfig.APPLICATION_ID_CONFIG] = "worktime-administration"
        configs[StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG] = WallclockTimestampExtractor::class.java.name
        configs[StreamsConfig.PROCESSING_GUARANTEE_CONFIG] = StreamsConfig.EXACTLY_ONCE
        configs[StreamsConfig.APPLICATION_SERVER_CONFIG] = "$hostname:8082"
        return KafkaStreamsConfiguration(configs)
    }

    @Bean
    fun createTableTopicFromStream(builder: StreamsBuilder): KStream<String, WorktimeEntry?> {
        return builder.stream<String, Event>(WORKTIME_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { key: String, value: Event, aggregate: WorktimeEntry? -> handleWorktimeEvent(value, aggregate) },
                        Materialized.`as`<String, WorktimeEntry, KeyValueStore<Bytes, ByteArray>>("$WORKTIME_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(worktimeSerde)
                )
                .toStream()
                .through("$WORKTIME_AGGREGATE-table", Produced.with(Serdes.String(), worktimeSerde))
    }

    @Bean
    fun createEmployeeStore(builder: StreamsBuilder): GlobalKTable<String, Employee> {
        return builder.globalTable(
                "$EMPLOYEE_AGGREGATE-table",
                Consumed.with(Serdes.String(), employeeSerde),
                Materialized.`as`("$EMPLOYEE_AGGREGATE-store")
        )
    }

    @Bean
    fun createProjectStore(builder: StreamsBuilder): GlobalKTable<String, Project> {
        return builder.globalTable(
                "$PROJECT_AGGREGATE-table",
                Consumed.with(Serdes.String(), projectSerde),
                Materialized.`as`("$PROJECT_AGGREGATE-store")
        )
    }

}