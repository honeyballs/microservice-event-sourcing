package com.example.projectadministration.configuration

import com.example.projectadministration.model.aggregates.CUSTOMER_AGGREGATE
import com.example.projectadministration.model.aggregates.PROJECT_AGGREGATE
import com.example.projectadministration.model.aggregates.employee.POSITION_AGGREGATE
import com.example.projectadministration.model.events.Event
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.transaction.KafkaTransactionManager

@Configuration
@EnableKafka
class KafkaProducerConfig {

    // We have to use the spring configured object mapper which is able to map kotlin
    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var env: Environment

    @Bean
    fun customerTopic(): NewTopic {
        return TopicBuilder.name(CUSTOMER_AGGREGATE)
                .partitions(2)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "-1")
                .build()
    }

    @Bean
    fun projectTopic(): NewTopic {
        return TopicBuilder.name(PROJECT_AGGREGATE)
                .partitions(2)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "-1")
                .build()
    }

    @Bean
    fun producerConfigs():Map<String, Any> {
        val configs = HashMap<String, Any>()
        configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = env.getProperty("KAFKA_URL", "localhost:9093")
        configs[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = true
        return configs
    }

    @Bean
    fun admin(): KafkaAdmin {
        return KafkaAdmin(producerConfigs())
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Event> {
        val serializer = JsonSerializer<Event>(mapper)
        serializer.isAddTypeInfo = false
        val factory = DefaultKafkaProducerFactory<String, Event>(producerConfigs(), StringSerializer(), serializer)
        val containerNr = env.getProperty("CONTAINER_NR", "")
        factory.setTransactionIdPrefix("project-administration${containerNrWithDot(containerNr)}")
        return factory
    }

    @Bean
    fun transactionManager(factory: ProducerFactory<String, Event>): KafkaTransactionManager<String, Event> {
        return KafkaTransactionManager(factory)
    }

    @Bean
    fun kafkaTemplate(factory: ProducerFactory<String, Event>): KafkaTemplate<String, Event> {
        return KafkaTemplate<String, Event>(factory)
    }

    fun containerNrWithDot(nr: String): String {
        if (nr != "") {
            return ".$nr"
        }
        return nr
    }
}