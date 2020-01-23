package com.example.worktimeadministration.model.aggregates


import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.worktime.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

const val WORKTIME_AGGREGATE = "worktime"
const val DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss"


@JsonTypeName("worktime")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
class WorktimeEntry(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) var startTime: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) var endTime: LocalDateTime,
        var pauseTimeInMinutes: Int = 0,
        var project: String,
        val employee: String,
        var description: String,
        var type: EntryType,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
) : Aggregate() {

    init {
        aggregateName = WORKTIME_AGGREGATE
        areTimesValid(startTime, endTime, pauseTimeInMinutes)
    }

    companion object {
        fun create(
                startTime: LocalDateTime,
                endTime: LocalDateTime,
                pauseTimeInMinutes: Int,
                project: String,
                employee: String,
                description: String,
                type: EntryType
        ): WorktimeEntry {
            val entry = WorktimeEntry(startTime, endTime, pauseTimeInMinutes, project, employee, description, type)
            entry.registerEvent(WorktimeCreated(entry.id, startTime, endTime, pauseTimeInMinutes, project, employee, description, type.name))
            return entry
        }
    }

    fun changeProject( project: String) {
        if (project == "") {
            throw RuntimeException("Project must be defined")
        }
        this.project = project
        registerEvent(WorktimeProjectChanged(this.project))
    }

    fun changeDescription(description: String) {
        if (description == "") {
            throw RuntimeException("Description must be defined")
        }
        this.description = description
        registerEvent(WorktimeDescriptionChanged(description))
    }

    fun adjustStartTime(newStartTime: LocalDateTime) {
        areTimesValid(newStartTime, this.endTime, this.pauseTimeInMinutes)
        this.startTime = newStartTime
        registerEvent(WorktimeStarttimeAdjusted(this.startTime))
    }

    fun adjustEndTime(newEndTime: LocalDateTime) {
        areTimesValid(this.startTime, newEndTime, this.pauseTimeInMinutes)
        this.endTime = newEndTime
        registerEvent(WorktimeEndtimeAdjusted(this.endTime))
    }

    fun adjustPause(pause: Int) {
        areTimesValid(this.startTime, this.endTime, pause)
        this.pauseTimeInMinutes = pause
        registerEvent(WorktimePauseAdjusted(this.pauseTimeInMinutes))
    }

    fun deleteEntry() {
        this.deleted = true
        registerEvent(WorktimeDeleted())
    }

    private fun isPauseTimeSufficient(startTime: LocalDateTime, endTime: LocalDateTime, pause: Int): Boolean {
        if (type != EntryType.WORK) {
            return true
        }
        val timespan = startTime.until(endTime, ChronoUnit.HOURS).toInt()
        return timespan < 8 || timespan in 8..9 && pause >= 30
                || timespan >= 10 && pause >= 60
    }

    @Throws(RuntimeException::class)
    private fun areTimesValid(startTime: LocalDateTime, endTime: LocalDateTime, pause: Int) {
        if (startTime.isAfter(endTime)) {
            throw RuntimeException("The start time must be after the end time")
        }
        if (!isPauseTimeSufficient(startTime, endTime, pause)) {
            throw RuntimeException("The pause time is insufficient")
        }
    }

    override fun toString(): String {
        return "Worktime Entry - Id: $id, Description: $description"
    }
}

enum class EntryType(type: String) {
    WORK("WORK"),
    VACATION("VACATION"),
    SICK("SICK")
}