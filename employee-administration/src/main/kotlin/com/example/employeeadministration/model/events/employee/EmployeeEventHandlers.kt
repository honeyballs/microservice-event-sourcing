package com.example.employeeadministration.model.events.employee

import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.valueobjects.Address
import com.example.employeeadministration.model.valueobjects.BankDetails
import com.example.employeeadministration.model.valueobjects.CompanyMail
import com.example.employeeadministration.model.valueobjects.ZipCode
import events.Event
import events.employee.*


fun handleEmployeeEvent(event: Event, employee: Employee?): Employee {
    var emp = employee
    if (emp == null) {
        if (event is EmployeeCreated) {
            emp = handleCreateEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent employee")
        }
    } else if (emp.deleted) {
        return emp
    } else {
        when (event) {
            is EmployeeChangedName -> handleNameChangedEvent(event, emp)
            is EmployeeMoved -> handleMovedEvent(event, emp)
            is EmployeeChangedBanking -> handleChangedBankingEvent(event, emp)
            is EmployeeChangedDepartment -> handleChangedDepartmentEvent(event, emp)
            is EmployeeChangedPosition -> handlePositionChangedEvent(event, emp)
            is EmployeeRateAdjusted -> handleAdjustedRateEvent(event, emp)
            is EmployeeDeleted -> handleDeletedEvent(event, emp)
        }
    }
    return emp
}

fun handleCreateEvent(event: EmployeeCreated): Employee {
    return Employee(
            event.firstname,
            event.lastname,
            event.birthday,
            Address(event.street, event.no, event.city, ZipCode(event.zipCode)),
            BankDetails(event.iban, event.bic, event.bankname),
            event.department,
            event.position,
            event.availableVacationHours,
            event.hourlyRate,
            CompanyMail(event.mail),
            event.employeeId
    )
}

fun handleNameChangedEvent(event: EmployeeChangedName, employee: Employee) {
    employee.firstname = event.firstname
    employee.lastname = event.lastname
    employee.companyMail = CompanyMail(event.mail)
}


fun handleMovedEvent(event: EmployeeMoved, employee: Employee) {
    employee.address = Address(event.street, event.no, event.city, ZipCode(event.zipCode))
}


fun handleChangedBankingEvent(event: EmployeeChangedBanking, employee: Employee) {
    employee.bankDetails = BankDetails(event.iban, event.bic, event.bankname)
}

fun handleChangedDepartmentEvent(event: EmployeeChangedDepartment, employee: Employee) {
    employee.department = event.department
}

fun handlePositionChangedEvent(event: EmployeeChangedPosition, employee: Employee) {
    employee.position = event.position
}

fun handleAdjustedRateEvent(event: EmployeeRateAdjusted, employee: Employee) {
    employee.hourlyRate = event.rate
}

fun handleDeletedEvent(event: EmployeeDeleted, employee: Employee) {
    employee.deleted = true
}