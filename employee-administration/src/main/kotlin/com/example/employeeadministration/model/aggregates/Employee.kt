package com.example.employeeadministration.model.aggregates

import com.example.employeeadministration.model.events.employee.*
import com.example.employeeadministration.model.valueobjects.Address
import com.example.employeeadministration.model.valueobjects.BankDetails
import com.example.employeeadministration.model.valueobjects.CompanyMail
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.employee.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

const val EMPLOYEE_AGGREGATE = "employee"
const val datePattern = "dd.MM.yyyy"

@JsonTypeName("employee")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
class Employee(
        var firstname: String,
        var lastname: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern) val birthday: LocalDate,
        var address: Address,
        var bankDetails: BankDetails,
        var department: String,
        var position: String,
        var availableVacationHours: Int,
        hourlyRate: BigDecimal,
        companyMail: CompanyMail?,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    // initialize it rounded. Apparently the custom setter is not applied to the initialization
    var hourlyRate: BigDecimal = hourlyRate.setScale(2, RoundingMode.HALF_UP)
        set(value) {
            // Always round the salary field
            field = value.setScale(2, RoundingMode.HALF_UP)
        }

    var companyMail = companyMail ?: CompanyMail(firstname, lastname)

    init {
        aggregateName = EMPLOYEE_AGGREGATE
    }

    companion object {
        fun create(firstname: String,
                   lastname: String,
                   birthday: LocalDate,
                   address: Address,
                   bankDetails: BankDetails,
                   department: String,
                   position: String,
                   availableVacationHours: Int,
                   hourlyRate: BigDecimal
        ): Employee {
            val employee = Employee(firstname, lastname, birthday, address, bankDetails, department, position, availableVacationHours, hourlyRate, null)
            employee.registerEvent(EmployeeCreated(
                    employee.id,
                    firstname,
                    lastname, birthday,
                    address.street,
                    address.no,
                    address.city,
                    address.zipCode.zip,
                    bankDetails.iban,
                    bankDetails.bic,
                    bankDetails.bankName,
                    department,
                    position,
                    availableVacationHours,
                    hourlyRate,
                    employee.companyMail.mail))
            return employee
        }
    }

    fun moveToNewAddress(address: Address) {
        this.address = address
        registerEvent(EmployeeMoved(this.address.street, this.address.no, this.address.city, this.address.zipCode.zip))
    }

    fun receiveRaiseBy(raiseAmount: BigDecimal) {
        this.hourlyRate = hourlyRate.add(raiseAmount).setScale(2, RoundingMode.HALF_UP)
        registerEvent(EmployeeRateAdjusted(this.hourlyRate))
    }

    fun switchBankDetails(bankDetails: BankDetails) {
        this.bankDetails = bankDetails
        registerEvent(EmployeeChangedBanking(this.bankDetails.iban, this.bankDetails.bic, this.bankDetails.bankName))
    }

    fun changeJobPosition(position: String) {
        this.position = position
        registerEvent(EmployeeChangedPosition(this.position))
    }

    fun moveToAnotherDepartment(department: String) {
        this.department = department
        registerEvent(EmployeeChangedDepartment(this.department))
    }

    /**
     * Change the name(s) of a employee which subsequently changes the mail address
     */
    fun changeName(firstname: String?, lastname: String?) {
        this.firstname = firstname ?: this.firstname
        this.lastname = lastname ?: this.lastname
        this.companyMail = CompanyMail(this.firstname, this.lastname)
        registerEvent(EmployeeChangedName(this.firstname, this.lastname, this.companyMail.mail))
    }

    fun delete() {
        deleted = true
        registerEvent(EmployeeDeleted())
    }

    override fun toString(): String {
        return "Employee - Id: $id, Name: $firstname, $lastname"
    }
}