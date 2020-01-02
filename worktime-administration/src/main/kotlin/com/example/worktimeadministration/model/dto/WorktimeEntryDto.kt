package com.example.worktimeadministration.model.dto

import com.example.worktimeadministration.model.employee.EmployeeDto
import com.example.worktimeadministration.model.events.DATE_TIME_PATTERN
import com.example.worktimeadministration.model.project.Project
import com.example.worktimeadministration.model.project.ProjectDto
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.*

data class WorktimeEntryDto(
        val id: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) val startTime: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) val endTime: LocalDateTime,
        val pauseTimeInMinutes: Int,
        val project: ProjectDto,
        val employee: EmployeeDto,
        val description: String
)