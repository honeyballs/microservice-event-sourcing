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
        JsonSubTypes.Type(value = WorktimeSetTime::class, name = "worktime-time-set"),
        JsonSubTypes.Type(value = WorktimeProjectSet::class, name = "worktime-project"),
        JsonSubTypes.Type(value = WorktimeDescriptionSet::class, name = "worktime-set-description"),
        JsonSubTypes.Type(value = WorktimeStarttimeAdjusted::class, name = "worktime-starttime-adjusted"),
        JsonSubTypes.Type(value = WorktimeEndtimeAdjusted::class, name = "worktime-endtime-adjusted"),
        JsonSubTypes.Type(value = WorktimePauseAdjusted::class, name = "worktime-pause-adjusted"),
        JsonSubTypes.Type(value = WorktimeDeleted::class, name = "worktime-deleted")
)
abstract class Event(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
) {

    open lateinit var type: String

}