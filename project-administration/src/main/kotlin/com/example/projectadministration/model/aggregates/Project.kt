package com.example.projectadministration.model.aggregates

import com.example.projectadministration.model.employee.Employee
import com.example.projectadministration.model.events.project.*
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
        var employees: Set<String>,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var endDate: LocalDate? = null,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    init {
        aggregateName = PROJECT_AGGREGATE
    }

    companion object {
        fun create(
                name: String,
                description: String,
                startDate: LocalDate,
                projectedEndDate: LocalDate,
                employees: Set<String>,
                endDate: LocalDate?
        ): Project {
            val project = Project(name, description, startDate, projectedEndDate, employees, endDate)
            project.registerEvent(ProjectCreated(project.id, name, description, startDate, projectedEndDate, employees))
            return project
        }
    }

    fun updateDescription(description: String) {
        this.description = description
        registerEvent(ProjectDescriptionChanged(this.description))
    }

    fun delayProject(projectedEndDate: LocalDate) {
        if (endDate == null && startDate.isBefore(projectedEndDate)) {
            this.projectedEndDate = projectedEndDate
            registerEvent(ProjectDelayed(this.projectedEndDate))
        } else {
            throw RuntimeException("The provided date is not valid")
        }
    }

    fun addEmployeeToProject(employeeId: String) {
        this.employees = this.employees.plus(employeeId)
        registerEvent(ProjectEmployeeAdded(employeeId))
    }

    fun removeEmployeeFromProject(employeeId: String) {
        this.employees = this.employees.minus(employeeId)
        registerEvent(ProjectEmployeeRemoved(employeeId))
    }

    fun delete() {
        this.deleted = true
        registerEvent(ProjectDeleted())
    }

}