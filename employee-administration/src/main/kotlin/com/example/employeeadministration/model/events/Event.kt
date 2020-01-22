package com.example.employeeadministration.model.events

import com.example.employeeadministration.model.events.department.DepartmentCreated
import com.example.employeeadministration.model.events.department.DepartmentDeleted
import com.example.employeeadministration.model.events.department.DepartmentNameChanged
import com.example.employeeadministration.model.events.employee.*
import com.example.employeeadministration.model.events.position.PositionCreated
import com.example.employeeadministration.model.events.position.PositionDeleted
import com.example.employeeadministration.model.events.position.PositionTitleChanged
import com.example.employeeadministration.model.events.position.PositionWageRangeChanged
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss:SSS"

@JsonTypeName("event")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
        JsonSubTypes.Type(value = EmployeeCreated::class, name = "employee-created"),
        JsonSubTypes.Type(value = EmployeeChangedName::class, name = "employee-change-name"),
        JsonSubTypes.Type(value = EmployeeMoved::class, name = "employee-moved"),
        JsonSubTypes.Type(value = EmployeeChangedBanking::class, name = "employee-changed-banking"),
        JsonSubTypes.Type(value = EmployeeChangedDepartment::class, name = "employee-changed-department"),
        JsonSubTypes.Type(value = EmployeeRateAdjusted::class, name = "employee-adjusted-rate"),
        JsonSubTypes.Type(value = EmployeeRateAdjusted::class, name = "employee-changed-position"),
        JsonSubTypes.Type(value = EmployeeDeleted::class, name = "employee-deleted"),

        JsonSubTypes.Type(value = DepartmentCreated::class, name = "department-created"),
        JsonSubTypes.Type(value = DepartmentNameChanged::class, name = "department-name-changed"),
        JsonSubTypes.Type(value = DepartmentDeleted::class, name = "department-deleted"),

        JsonSubTypes.Type(value = PositionCreated::class, name = "position-created"),
        JsonSubTypes.Type(value = PositionTitleChanged::class, name = "position-title-changed"),
        JsonSubTypes.Type(value = PositionWageRangeChanged::class, name = "position-wage-range-changed"),
        JsonSubTypes.Type(value = PositionDeleted::class, name = "position-deleted")
)
abstract class Event(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
) {

    open lateinit var type: String

}