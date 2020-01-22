package com.example.projectadministration.model.aggregates

import com.example.projectadministration.model.aggregates.employee.Employee
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
        var name: String,
        var description: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var projectedEndDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) var endDate: LocalDate?,
        var employees: Set<String>,
        val customer: String,
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
                endDate: LocalDate?,
                employees: Set<String>,
                customer: String
        ): Project {
            val project = Project(name, description, startDate, projectedEndDate, endDate, employees, customer)
            project.registerEvent(ProjectCreated(project.id, name, description, startDate, projectedEndDate, endDate, employees, customer))
            return project
        }
    }

    fun delayProject(newProjectedDate: LocalDate) {
        this.projectedEndDate = newProjectedDate
        registerEvent(ProjectDelayed(this.projectedEndDate))
    }

    fun finishProject(endDate: LocalDate) {
        if (this.endDate == null) {
            this.endDate = endDate
        } else {
            throw RuntimeException("Project is already finished")
        }
        registerEvent(ProjectFinished(this.endDate!!))
    }

    fun updateProjectDescription(description: String) {
        this.description = description
        registerEvent(ProjectDescriptionChanged(this.description))
    }

    fun addEmployeeToProject(employee: String) {
        this.employees = employees.plus(employee)
        registerEvent(ProjectEmployeeAdded(employee))
    }

    fun removeEmployeeFromProject(employee: String) {
        this.employees = employees.minus(employee)
        registerEvent(ProjectEmployeeRemoved(employee))
    }

    fun changeEmployeesWorkingOnProject(employees: Set<String>) {
        this.employees = employees
        registerEvent(ProjectEmployeesChanged(this.employees))
    }

    fun delete() {
        this.deleted = true
        registerEvent(ProjectDeleted())
    }

    override fun toString(): String {
        return "Project - Id: $id, name: $name"
    }
}