package com.example.employeeadministration.model.events.employee

import com.example.employeeadministration.model.events.Event
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.math.BigDecimal

@JsonTypeName("employee-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeCreated(
        val employeeId: String,
        val firstname: String,
        val lastname: String,
        val address: String,
        val mail: String,
        val iban: String,
        val department: String,
        val title: String,
        val hourlyRate: BigDecimal
) : Event() {

    init {
        type = "Employee created"
    }

}

@JsonTypeName("employee-first-name")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedFirstName(
        val firstname: String
) : Event() {

    init {
        type = "Employee changed first name"
    }

}

@JsonTypeName("employee-last-name")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedLastName(
        val lastname: String
) : Event() {

    init {
        type = "Employee changed last name"
    }

}

@JsonTypeName("employee-moved")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeMoved(
        val address: String
) : Event() {

    init {
        type = "Employee moved"
    }

}

@JsonTypeName("employee-changed-mail")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedMail(
        val mail: String
) : Event() {

    init {
        type = "Employee changed mail"
    }

}

@JsonTypeName("employee-adjusted-rate")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeRateAdjusted(
        val rate: BigDecimal
) : Event() {

    init {
        type = "Employee rate adjusted"
    }

}

@JsonTypeName("employee-changed-department")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedDepartment(
        val department: String
) : Event() {

    init {
        type = "Employee changed department"
    }

}

@JsonTypeName("employee-changed-title")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedTitle(
        val title: String
) : Event() {

    init {
        type = "Employee changed title"
    }

}

@JsonTypeName("employee-changed-banking")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedBanking(
        val iban: String
) : Event() {

    init {
        type = "Employee changed banking"
    }

}

@JsonTypeName("employee-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeDeleted(
        val deleted: Boolean = true
) : Event() {

    init {
        type = "Employee deleted"
    }

}