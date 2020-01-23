package com.example.worktimeadministration.configuration


import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.VACATION_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WORKTIME_AGGREGATE
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.aggregates.employee.EMPLOYEE_AGGREGATE
import com.example.worktimeadministration.model.aggregates.employee.Employee
import com.example.worktimeadministration.model.events.worktime.handleWorktimeEvent
import com.example.worktimeadministration.model.aggregates.project.PROJECT_AGGREGATE
import com.example.worktimeadministration.model.aggregates.project.Project
import com.example.worktimeadministration.model.events.worktime.handleVacationHoursEvent
import com.fasterxml.jackson.databind.ObjectMapper
import events.Event
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
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
import org.springframework.kafka.config.TopicBuilder
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
    lateinit var usedVacationSerde: Serde<UsedEmployeeVacationHours>
    lateinit var worktimeSerde: Serde<WorktimeEntry>

    init {
        val eventDeserializer = JsonDeserializer(Event::class.java, mapper)
        eventDeserializer.setUseTypeHeaders(false)
        val employeeDeserializer = JsonDeserializer(Employee::class.java, mapper)
        employeeDeserializer.setUseTypeHeaders(false)
        val projectDeserializer = JsonDeserializer(Project::class.java, mapper)
        projectDeserializer.setUseTypeHeaders(false)
        val usedVacationDeserializer = JsonDeserializer(UsedEmployeeVacationHours::class.java, mapper)
        usedVacationDeserializer.setUseTypeHeaders(false)
        val worktimeDeserializer = JsonDeserializer(WorktimeEntry::class.java, mapper)
        worktimeDeserializer.setUseTypeHeaders(false)
        eventSerde = Serdes.serdeFrom(JsonSerializer<Event>(mapper), eventDeserializer)
        employeeSerde = Serdes.serdeFrom(JsonSerializer<Employee>(mapper), employeeDeserializer)
        projectSerde = Serdes.serdeFrom(JsonSerializer<Project>(mapper), projectDeserializer)
        usedVacationSerde = Serdes.serdeFrom(JsonSerializer<UsedEmployeeVacationHours>(mapper), usedVacationDeserializer)
        worktimeSerde = Serdes.serdeFrom(JsonSerializer<WorktimeEntry>(mapper), worktimeDeserializer)
    }

    @Bean
    fun vacationStoreTopic(): NewTopic {
        return TopicBuilder.name("$VACATION_AGGREGATE-table")
                .partitions(2)
                .replicas(1)
                .compact()
                .config(TopicConfig.RETENTION_MS_CONFIG, "2000")
                .build()
    }

    @Bean
    fun worktimeStoreTopic(): NewTopic {
        return TopicBuilder.name("$WORKTIME_AGGREGATE-table")
                .partitions(2)
                .replicas(1)
                .compact()
                .config(TopicConfig.RETENTION_MS_CONFIG, "2000")
                .build()
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
        configs[StreamsConfig.APPLICATION_SERVER_CONFIG] = "$hostname:8085"
        return KafkaStreamsConfiguration(configs)
    }

    @Bean
    fun createVacationTableTopicFromStream(builder: StreamsBuilder): KStream<String, UsedEmployeeVacationHours?> {
        return builder.stream<String, Event>(VACATION_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { _: String, value: Event, aggregate: UsedEmployeeVacationHours? -> handleVacationHoursEvent(value, aggregate) },
                        Materialized.`as`<String, UsedEmployeeVacationHours, KeyValueStore<Bytes, ByteArray>>("$VACATION_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(usedVacationSerde)
                )
                .toStream()
                .peek(ForeachAction { _: String, value: UsedEmployeeVacationHours  -> println(value.toString()) })
                .through("$VACATION_AGGREGATE-table", Produced.with(Serdes.String(), usedVacationSerde))
    }

    @Bean
    fun createProjectTableTopicFromStream(builder: StreamsBuilder): KStream<String, WorktimeEntry?> {
        return builder.stream<String, Event>(WORKTIME_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { _: String, value: Event, aggregate: WorktimeEntry? -> handleWorktimeEvent(value, aggregate) },
                        Materialized.`as`<String, WorktimeEntry, KeyValueStore<Bytes, ByteArray>>("$WORKTIME_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(worktimeSerde)
                )
                .toStream()
                .peek(ForeachAction { _: String, value: WorktimeEntry  -> println(value.toString()) })
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