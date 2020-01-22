package com.example.employeeadministration.model.dto

import com.example.employeeadministration.model.aggregates.datePattern
import com.example.employeeadministration.model.valueobjects.Address
import com.example.employeeadministration.model.valueobjects.BankDetails
import com.example.employeeadministration.model.valueobjects.CompanyMail
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

/**
 * This file collects DTO classes to fulfill requests. These DTO's only have immutable fields because all data changes are performed on entities
 */
data class DepartmentDto(val id: String?, val name: String)

data class PositionDto(val id: String?, val title: String, val minHourlyWage: BigDecimal, val maxHourlyWage: BigDecimal)

data class EmployeeDto(val id: String?,
                       val firstname: String,
                       val lastname: String,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern) val birthday: LocalDate,
                       val address: Address,
                       val bankDetails: BankDetails,
                       val department: DepartmentDto,
                       val position: PositionDto,
                       val hourlyRate: BigDecimal,
                       val availableVacationHours: Int,
                       val companyMail: CompanyMail?
)