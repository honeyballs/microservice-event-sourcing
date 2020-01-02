package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.aggregates.Aggregate
import com.example.worktimeadministration.model.events.Event
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventProducer(val kafkaTemplate: KafkaTemplate<String, Event>) : AggregateEventProducingService {

    /**
     * We run the production of events in a transaction.
     * This guarantees conflicting updates between instances of the same service.
     *
     * If another instance of this service were to complete it's own event producing transaction
     * this update would not go through. Each transaction gets an incremented id,
     * if this transaction id is lower than a transaction that was completed before the transaction is cancelled.
     */
    override fun produceAggregateEvent(agg: Aggregate) {
        kafkaTemplate.executeInTransaction { t ->
            agg.events.map { ProducerRecord<String, Event>(agg.aggregateName, agg.id, it) }
                    .forEach { t.send(it) }
            agg.clearEvents()
        }

    }

}