package com.example.employeeadministration.model.events.employee

import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.events.Event


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
            event.address,
            event.bankDetails,
            event.department,
            event.position,
            event.availableVacationHours,
            event.hourlyRate,
            event.mail,
            event.employeeId
    )
}

fun handleNameChangedEvent(event: EmployeeChangedName, employee: Employee) {
    employee.firstname = event.firstname
    employee.lastname = event.lastname
    employee.companyMail = event.mail
}


fun handleMovedEvent(event: EmployeeMoved, employee: Employee) {
    employee.address = event.address
}


fun handleChangedBankingEvent(event: EmployeeChangedBanking, employee: Employee) {
    employee.bankDetails = event.bankDetails
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