package com.example.worktimeadministration.model.aggregates

import com.example.worktimeadministration.model.events.Event
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("aggregate")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
        JsonSubTypes.Type(value = WorktimeEntry::class, name = "worktime")
)
abstract class Aggregate {

    open lateinit var aggregateName: String
    open lateinit var id: String

    @JsonIgnore
    val events = mutableListOf<Event>()

    fun registerEvent(event: Event) {
        events.add(event)
    }

    fun clearEvents() {
        events.clear()
    }

}