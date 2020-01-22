package com.example.worktimeadministration.model.dto

import com.example.worktimeadministration.model.aggregates.EntryType
import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.project.DATE_PATTERN
import com.example.worktimeadministration.model.events.DATE_TIME_PATTERN
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime

class EmployeeDto(
        val id: String,
        val firstname: String,
        val lastname: String,
        val availableVacationHours: Int,
        val usedHours: Int
)

class ProjectDto(
        val id: String,
        val name: String,
        val description: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val projectedEndDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val endDate: LocalDate?
)

class WorktimeEntryDto(
        val id: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) var startTime: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) var endTime: LocalDateTime,
        var pauseTimeInMinutes: Int = 0,
        val project: ProjectDto,
        val employee: EmployeeDto,
        var description: String,
        var type: EntryType
)