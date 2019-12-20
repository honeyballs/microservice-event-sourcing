package com.example.employeeadministration.model.aggregates

import com.example.employeeadministration.model.events.employee.*
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.math.BigDecimal
import java.util.*

const val EMPLOYEE_AGGREGATE = "employee"

@JsonTypeName("employee")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class Employee(
        var firstname: String,
        var lastname: String,
        var address: String,
        var mail: String,
        var iban: String,
        var department: String,
        var title: String,
        var hourlyRate: BigDecimal,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    init {
        aggregateName = EMPLOYEE_AGGREGATE
    }

    companion object {
        fun create(firstname: String,
                   lastname: String,
                   address: String,
                   mail: String,
                   iban: String,
                   department: String,
                   title: String,
                   hourlyRate: BigDecimal
        ): Employee {
            val employee = Employee(firstname, lastname, address, mail, iban, department, title, hourlyRate)
            employee.registerEvent(EmployeeCreated(employee.id, firstname, lastname, address, mail, iban, department, title, hourlyRate))
            return employee
        }
    }

    fun changesName(firstname: String?, lastname: String?) {
        if (firstname != null && this.firstname != firstname) {
            this.firstname = firstname
            registerEvent(EmployeeChangedFirstName(this.firstname))
        }
        if (lastname != null && this.lastname != lastname) {
            this.lastname = lastname
            registerEvent(EmployeeChangedLastName(this.lastname))
        }
    }

    fun moves(address: String) {
        this.address = address
        registerEvent(EmployeeMoved(this.address))
    }

    fun changesMail(mail: String) {
        this.mail = mail
        registerEvent(EmployeeChangedMail(this.mail))
    }

    fun changesBanking(iban: String) {
        this.iban = iban
        registerEvent(EmployeeChangedBanking(this.iban))
    }

    fun adjustRate(rate: BigDecimal) {
        this.hourlyRate = rate
        registerEvent(EmployeeRateAdjusted(this.hourlyRate))
    }

    fun movesToDepartment(department: String) {
        this.department = department
        registerEvent(EmployeeChangedDepartment(this.department))
    }

    fun changesTitle(title: String) {
        this.title = title
        registerEvent(EmployeeChangedTitle(this.title))
    }

    fun delete() {
        this.deleted = true
        registerEvent(EmployeeDeleted())
    }
}