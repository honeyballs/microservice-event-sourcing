package com.example.projectadministration.model.dto

import com.example.projectadministration.model.aggregates.DATE_PATTERN
import com.example.projectadministration.model.employee.EmployeeDto
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.util.*


data class ProjectDto(
        val id: String?,
        val name: String,
        var description: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var projectedEndDate: LocalDate,
        var employees: Set<EmployeeDto>,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var endDate: LocalDate? = null
)