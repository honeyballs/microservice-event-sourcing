package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.aggregates.Aggregate


interface AggregateEventProducingService {

    fun produceAggregateEvent(agg: Aggregate)

}