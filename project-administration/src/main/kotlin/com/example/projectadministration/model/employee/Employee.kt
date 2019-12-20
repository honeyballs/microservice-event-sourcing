package com.example.projectadministration.model.employee

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

const val EMPLOYEE_AGGREGATE = "employee"

@JsonIgnoreProperties(ignoreUnknown = true)
data class Employee(
        val id: String,
        val firstname: String,
        val lastname: String,
        var mail: String,
        var department: String,
        var title: String,
        var deleted: Boolean
)