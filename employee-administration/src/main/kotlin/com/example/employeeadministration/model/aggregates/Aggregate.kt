package com.example.employeeadministration.model.aggregates

import com.fasterxml.jackson.annotation.*
import events.Event

@JsonTypeName("aggregate")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
        JsonSubTypes.Type(value = Department::class, name = "department"),
        JsonSubTypes.Type(value = Position::class, name = "position"),
        JsonSubTypes.Type(value = Employee::class, name = "employee")
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