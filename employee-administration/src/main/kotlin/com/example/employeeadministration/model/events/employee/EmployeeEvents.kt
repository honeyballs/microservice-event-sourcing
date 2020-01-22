package com.example.employeeadministration.model.events.employee

import com.example.employeeadministration.model.events.Event
import com.example.employeeadministration.model.valueobjects.Address
import com.example.employeeadministration.model.valueobjects.BankDetails
import com.example.employeeadministration.model.valueobjects.CompanyMail
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.math.BigDecimal
import java.time.LocalDate

@JsonTypeName("employee-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeCreated(
        val employeeId: String,
        val firstname: String,
        val lastname: String,
        val birthday: LocalDate,
        val address: Address,
        val bankDetails: BankDetails,
        val department: String,
        val position: String,
        val availableVacationHours: Int,
        val hourlyRate: BigDecimal,
        val mail: CompanyMail
) : Event() {

    init {
        type = "Employee created"
    }

}

@JsonTypeName("employee-change-name")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedName(
        val firstname: String,
        val lastname: String,
        val mail: CompanyMail
) : Event() {

    init {
        type = "Employee changed name"
    }

}

@JsonTypeName("employee-moved")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeMoved(
        val address: Address
) : Event() {

    init {
        type = "Employee moved"
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

@JsonTypeName("employee-changed-banking")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedBanking(
        val bankDetails: BankDetails
) : Event() {

    init {
        type = "Employee changed banking"
    }

}

@JsonTypeName("employee-changed-position")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class EmployeeChangedPosition(
        val position: String
) : Event() {

    init {
        type = "Employee changed job position"
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