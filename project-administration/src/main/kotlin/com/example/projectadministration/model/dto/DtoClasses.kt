package com.example.projectadministration.model.dto


import com.example.projectadministration.model.aggregates.Address
import com.example.projectadministration.model.aggregates.CustomerContact
import com.example.projectadministration.model.aggregates.DATE_PATTERN
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class CustomerDto(
        val id: String?,
        val customerName: String,
        val address: Address,
        val contact: CustomerContact
)

data class ProjectDto(
        val id: String?,
        val name: String,
        val description: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val projectedEndDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val endDate: LocalDate?,
        val projectEmployees: Set<ProjectEmployeeDto>,
        val customer: ProjectCustomerDto
)

data class ProjectEmployeeDto(val id: String, val firstname: String, val lastname: String, val mail: String)

data class ProjectCustomerDto(val id: String, val customerName: String)

data class EmployeeDto(
    val id: String,
    val firstname: String,
    val lastname: String,
    val department: String,
    val position: String,
    val companyMail: String
)
