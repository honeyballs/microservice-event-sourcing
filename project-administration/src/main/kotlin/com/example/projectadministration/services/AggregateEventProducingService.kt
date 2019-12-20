package com.example.projectadministration.services

import com.example.projectadministration.model.aggregates.Aggregate


interface AggregateEventProducingService {

    fun produceAggregateEvent(agg: Aggregate)

}