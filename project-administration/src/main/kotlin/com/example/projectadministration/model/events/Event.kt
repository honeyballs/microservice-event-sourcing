package com.example.projectadministration.model.events

import com.example.projectadministration.model.aggregates.Aggregate
import com.example.projectadministration.model.events.project.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss:SSS"

@JsonTypeName("event")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
        JsonSubTypes.Type(value = ProjectCreated::class, name = "project-created"),
        JsonSubTypes.Type(value = ProjectDescriptionChanged::class, name = "project-description-changed"),
        JsonSubTypes.Type(value = ProjectDelayed::class, name = "project-delayed"),
        JsonSubTypes.Type(value = ProjectEmployeeAdded::class, name = "project-employee-added"),
        JsonSubTypes.Type(value = ProjectEmployeeRemoved::class, name = "project-employee-removed"),
        JsonSubTypes.Type(value = ProjectFinished::class, name = "project-finished"),
        JsonSubTypes.Type(value = ProjectDeleted::class, name = "project-deleted")
)
abstract class Event(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
) {

    open lateinit var type: String

}