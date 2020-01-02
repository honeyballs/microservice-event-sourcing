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
            is EmployeeChangedFirstName -> handleFirstnameEvent(event, emp)
            is EmployeeChangedLastName -> handleLastnameEvent(event, emp)
            is EmployeeMoved -> handleMovedEvent(event, emp)
            is EmployeeChangedMail -> handleChangedMailEvent(event, emp)
            is EmployeeChangedBanking -> handleChangedBankingEvent(event, emp)
            is EmployeeChangedDepartment -> handleChangedDepartmentEvent(event, emp)
            is EmployeeChangedTitle -> handleChangedTitleEvent(event, emp)
            is EmployeeRateAdjusted -> handleAdjustedRateEvent(event, emp)
        }
    }
    return emp
}

fun handleCreateEvent(event: EmployeeCreated): Employee {
    return Employee(
            event.firstname,
            event.lastname,
            event.address,
            event.mail,
            event.iban,
            event.department,
            event.title,
            event.hourlyRate,
            event.employeeId
    )
}

fun handleFirstnameEvent(event: EmployeeChangedFirstName, employee: Employee): Employee {
    employee.firstname = event.firstname
    return employee
}

fun handleLastnameEvent(event: EmployeeChangedLastName, employee: Employee) {
    employee.lastname = event.lastname
}

fun handleMovedEvent(event: EmployeeMoved, employee: Employee) {
    employee.address = event.address
}

fun handleChangedMailEvent(event: EmployeeChangedMail, employee: Employee) {
    employee.mail = event.mail
}

fun handleChangedBankingEvent(event: EmployeeChangedBanking, employee: Employee) {
    employee.iban = event.iban
}

fun handleChangedDepartmentEvent(event: EmployeeChangedDepartment, employee: Employee) {
    employee.department = event.department
}

fun handleChangedTitleEvent(event: EmployeeChangedTitle, employee: Employee) {
    employee.title = event.title
}

fun handleAdjustedRateEvent(event: EmployeeRateAdjusted, employee: Employee) {
    employee.hourlyRate = event.rate
}

fun handleDeletedEvent(event: EmployeeDeleted, employee: Employee) {
    employee.deleted = true
}