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

fun handleLastnameEvent(event: EmployeeChangedLastName, employee: Employee): Employee {
    employee.lastname = event.lastname
    return employee
}

fun handleMovedEvent(event: EmployeeMoved, employee: Employee): Employee {
    employee.address = event.address
    return employee
}

fun handleChangedMailEvent(event: EmployeeChangedMail, employee: Employee): Employee {
    employee.mail = event.mail
    return employee
}

fun handleChangedBankingEvent(event: EmployeeChangedBanking, employee: Employee): Employee {
    employee.iban = event.iban
    return employee
}

fun handleChangedDepartmentEvent(event: EmployeeChangedDepartment, employee: Employee): Employee {
    employee.department = event.department
    return employee
}

fun handleChangedTitleEvent(event: EmployeeChangedTitle, employee: Employee): Employee {
    employee.title = event.title
    return employee
}

fun handleAdjustedRateEvent(event: EmployeeRateAdjusted, employee: Employee): Employee {
    employee.hourlyRate = event.rate
    return employee
}

fun handleDeletedEvent(event: EmployeeDeleted, employee: Employee): Employee {
    employee.deleted = true
    return employee
}