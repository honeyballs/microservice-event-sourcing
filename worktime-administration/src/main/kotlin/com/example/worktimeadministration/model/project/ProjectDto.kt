package com.example.worktimeadministration.model.project

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class ProjectDto(
        val id: String,
        val name: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val endDate: LocalDate?
)