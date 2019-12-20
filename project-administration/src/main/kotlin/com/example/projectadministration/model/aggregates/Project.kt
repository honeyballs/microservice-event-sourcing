package com.example.projectadministration.model.aggregates

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDate
import java.util.*

const val DATE_PATTERN = "dd.MM.yyyy"
const val PROJECT_AGGREGATE = "project"

@JsonTypeName("project")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class Project(
        val name: String,
        var description: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var projectedEndDate: LocalDate,
        var employees: List<String>,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var endDate: LocalDate? = null,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    init {
        aggregateName = PROJECT_AGGREGATE
    }

}