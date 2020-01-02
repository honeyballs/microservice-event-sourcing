package com.example.worktimeadministration.model.aggregates

import com.example.worktimeadministration.model.employee.Employee
import com.example.worktimeadministration.model.events.DATE_TIME_PATTERN
import com.example.worktimeadministration.model.events.worktime.WorktimeCreated
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

const val WORKTIME_AGGREGATE = "worktime"

@JsonTypeName("worktime")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
class WorktimeEntry(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) var startTime: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) var endTime: LocalDateTime,
        var pauseTimeInMinutes: Int,
        var project: String,
        val employee: String,
        var description: String,
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
                description: String
        ): WorktimeEntry {
            val entry = WorktimeEntry(startTime, endTime, pauseTimeInMinutes, project, employee, description)
            entry.registerEvent(WorktimeCreated(entry.id, startTime, endTime, pauseTimeInMinutes, project, employee, description))
            return entry
        }
    }

    fun forProject( project: String) {
        if (project == "") {
            throw RuntimeException("Project must be defined")
        }
        this.project = project
    }

    fun enterDescription(description: String) {
        if (description == "") {
            throw RuntimeException("Description must be defined")
        }
        this.description = description
    }

    fun adjustStartTime(newStartTime: LocalDateTime) {
        areTimesValid(newStartTime, this.endTime, this.pauseTimeInMinutes)
        this.startTime = newStartTime
    }

    fun adjustEndTime(newEndTime: LocalDateTime) {
        areTimesValid(this.startTime, newEndTime, this.pauseTimeInMinutes)
        this.endTime = newEndTime
    }

    fun adjustPause(pause: Int) {
        areTimesValid(this.startTime, this.endTime, pause)
        this.pauseTimeInMinutes = pause
    }

    fun deleteEntry() {
        this.deleted = true
    }

    private fun isPauseTimeSufficient(startTime: LocalDateTime, endTime: LocalDateTime, pause: Int): Boolean {
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
}