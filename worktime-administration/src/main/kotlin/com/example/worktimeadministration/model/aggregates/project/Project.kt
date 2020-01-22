package com.example.worktimeadministration.model.aggregates.project

import com.example.worktimeadministration.model.aggregates.employee.Employee
import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDate

const val PROJECT_AGGREGATE= "project"
const val DATE_PATTERN = "dd.MM.yyyy"

data class Project(
        val id: String,
        var name: String,
        var description: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var projectedEndDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var endDate: LocalDate?,
        var employees: Set<String>,
        var deleted: Boolean
){
}