package com.example.worktimeadministration.model.events

import com.example.worktimeadministration.model.events.worktime.*
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
        JsonSubTypes.Type(value = WorktimeCreated::class, name = "worktime-created"),
        JsonSubTypes.Type(value = WorktimeProjectChanged::class, name = "worktime-project-changed"),
        JsonSubTypes.Type(value = WorktimeDescriptionChanged::class, name = "worktime-description-changed"),
        JsonSubTypes.Type(value = WorktimeStarttimeAdjusted::class, name = "worktime-starttime-adjusted"),
        JsonSubTypes.Type(value = WorktimeEndtimeAdjusted::class, name = "worktime-endtime-adjusted"),
        JsonSubTypes.Type(value = WorktimePauseAdjusted::class, name = "worktime-pause-adjusted"),
        JsonSubTypes.Type(value = WorktimeDeleted::class, name = "worktime-deleted"),

        JsonSubTypes.Type(value = UsedHoursCreated::class, name = "used-hours-created"),
        JsonSubTypes.Type(value = UsedHoursUpdated::class, name = "used-hours-updated"),
        JsonSubTypes.Type(value = UsedHoursDeleted::class, name = "used-hours-deleted")
)
abstract class Event(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
) {

    open lateinit var type: String

}