package com.example.employeeadministration.model.dto

import java.math.BigDecimal
import java.util.*

data class EmployeeDto(
        var firstname: String,
        var lastname: String,
        var address: String,
        var mail: String,
        var iban: String,
        var department: String,
        var title: String,
        var hourlyRate: BigDecimal,
        val id: String?
)