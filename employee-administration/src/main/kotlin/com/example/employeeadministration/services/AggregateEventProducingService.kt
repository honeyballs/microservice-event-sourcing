package com.example.employeeadministration.services

import com.example.employeeadministration.model.aggregates.Aggregate
import com.example.employeeadministration.model.events.EventType
import org.springframework.stereotype.Service

interface AggregateEventProducingService {

    fun produceAggregateEvent(agg: Aggregate)

}