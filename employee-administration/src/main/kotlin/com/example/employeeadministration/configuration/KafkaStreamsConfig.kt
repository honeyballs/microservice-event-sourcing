package com.example.employeeadministration.configuration

import com.example.employeeadministration.model.aggregates.*
import com.example.employeeadministration.model.events.department.handleDepartmentEvent
import com.example.employeeadministration.model.events.employee.*
import com.example.employeeadministration.model.events.position.handlePositionEvent
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
    lateinit var departmentSerde: Serde<Department>
    lateinit var positionSerde: Serde<Position>

    init {
        val eventDeserializer = JsonDeserializer(Event::class.java, mapper)
        eventDeserializer.setUseTypeHeaders(false)
        val employeeDeserializer = JsonDeserializer(Employee::class.java, mapper)
        employeeDeserializer.setUseTypeHeaders(false)
        val departmentDeserializer = JsonDeserializer(Department::class.java, mapper)
        departmentDeserializer.setUseTypeHeaders(false)
        val positionDeserializer = JsonDeserializer(Position::class.java, mapper)
        positionDeserializer.setUseTypeHeaders(false)
        eventSerde = Serdes.serdeFrom(JsonSerializer<Event>(mapper), eventDeserializer)
        employeeSerde = Serdes.serdeFrom(JsonSerializer<Employee>(mapper), employeeDeserializer)
        departmentSerde = Serdes.serdeFrom(JsonSerializer<Department>(mapper), departmentDeserializer)
        positionSerde = Serdes.serdeFrom(JsonSerializer<Position>(mapper), positionDeserializer)
    }

    @Bean
    fun employeeStoreTopic(): NewTopic {
        return TopicBuilder.name("$EMPLOYEE_AGGREGATE-table")
                .partitions(2)
                .replicas(1)
                .compact()
                .config(TopicConfig.RETENTION_MS_CONFIG, "2000")
                .build()
    }

    @Bean
    fun departmentStoreTopic(): NewTopic {
        return TopicBuilder.name("$DEPARTMENT_AGGREGATE-table")
                .partitions(2)
                .replicas(1)
                .compact()
                .config(TopicConfig.RETENTION_MS_CONFIG, "2000")
                .build()
    }

    @Bean
    fun positionStoreTopic(): NewTopic {
        return TopicBuilder.name("$POSITION_AGGREGATE-table")
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
        configs[StreamsConfig.APPLICATION_ID_CONFIG] = "employee-administration"
        configs[StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG] = WallclockTimestampExtractor::class.java.name
        configs[StreamsConfig.APPLICATION_SERVER_CONFIG] = "$hostname:8080"
        configs[StreamsConfig.PROCESSING_GUARANTEE_CONFIG] = StreamsConfig.EXACTLY_ONCE
        return KafkaStreamsConfiguration(configs)
    }

    @Bean
    fun createTableTopicFromEmployeeStream(builder: StreamsBuilder): KStream<String, Employee?> {
        return builder.stream<String, Event>(EMPLOYEE_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { _: String, value: Event, aggregate: Employee? -> handleEmployeeEvent(value, aggregate) },
                        Materialized.`as`<String, Employee, KeyValueStore<Bytes, ByteArray>>("$EMPLOYEE_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(employeeSerde)
                )
                .toStream()
                .peek(ForeachAction { _: String, value: Employee  -> println(value.toString()) })
                .through("$EMPLOYEE_AGGREGATE-table", Produced.with(Serdes.String(), employeeSerde))
    }

    @Bean
    fun createTableTopicFromDepartmentStream(builder: StreamsBuilder): KStream<String, Department?> {
        return builder.stream<String, Event>(DEPARTMENT_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { _: String, value: Event, aggregate: Department? -> handleDepartmentEvent(value, aggregate) },
                        Materialized.`as`<String, Department, KeyValueStore<Bytes, ByteArray>>("$DEPARTMENT_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(departmentSerde)
                )
                .toStream()
                .peek(ForeachAction { _: String, value: Department  -> println(value.toString()) })
                .through("$DEPARTMENT_AGGREGATE-table", Produced.with(Serdes.String(), departmentSerde))
    }

    @Bean
    fun createTableTopicFromPositionStream(builder: StreamsBuilder): KStream<String, Position?> {
        return builder.stream<String, Event>(POSITION_AGGREGATE, Consumed.with(Serdes.String(), eventSerde))
                .groupByKey()
                .aggregate(
                        { null },
                        { _: String, value: Event, aggregate: Position? -> handlePositionEvent(value, aggregate) },
                        Materialized.`as`<String, Position, KeyValueStore<Bytes, ByteArray>>("$POSITION_AGGREGATE-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(positionSerde)
                )
                .toStream()
                .peek(ForeachAction { _: String, value: Position  -> println(value.toString()) })
                .through("$POSITION_AGGREGATE-table", Produced.with(Serdes.String(), positionSerde))
    }

}