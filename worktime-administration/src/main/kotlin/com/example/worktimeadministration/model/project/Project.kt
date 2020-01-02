package com.example.worktimeadministration.model.project

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate
import java.util.*

const val DATE_PATTERN = "dd.MM.yyyy"
const val PROJECT_AGGREGATE = "project"

@JsonIgnoreProperties(ignoreUnknown = true)
data class Project(
        val id: String,
        val name: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val endDate: LocalDate?,
        val employees: Set<String>,
        val deleted: Boolean
)