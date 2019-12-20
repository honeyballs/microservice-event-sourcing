package com.example.employeeadministration.model.events

import com.example.employeeadministration.model.events.employee.*
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
        JsonSubTypes.Type(value = EmployeeChangedFirstName::class, name = "employee-first-name"),
        JsonSubTypes.Type(value = EmployeeChangedLastName::class, name = "employee-last-name"),
        JsonSubTypes.Type(value = EmployeeMoved::class, name = "employee-moved"),
        JsonSubTypes.Type(value = EmployeeChangedMail::class, name = "employee-changed-mail"),
        JsonSubTypes.Type(value = EmployeeChangedBanking::class, name = "employee-changed-banking"),
        JsonSubTypes.Type(value = EmployeeChangedDepartment::class, name = "employee-changed-department"),
        JsonSubTypes.Type(value = EmployeeChangedTitle::class, name = "employee-changed-title"),
        JsonSubTypes.Type(value = EmployeeRateAdjusted::class, name = "employee-adjusted-rate"),
        JsonSubTypes.Type(value = EmployeeDeleted::class, name = "employee-deleted")
)
abstract class Event(
        val id: String = UUID.randomUUID().toString(),
        val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
) {

    open lateinit var type: String

}