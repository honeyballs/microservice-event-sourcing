package com.example.employeeadministration.model.events.department

import com.example.employeeadministration.model.aggregates.Department
import com.example.employeeadministration.model.events.Event

fun handleDepartmentEvent(event: Event, department: Department?): Department {
    var dep = department
    if (dep == null) {
        println(event.toString())
        if (event is DepartmentCreated) {
            dep = handleCreateEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent department")
        }
    } else if (dep.deleted) {
        return dep
    } else {
        when (event) {
            is DepartmentNameChanged -> handleNameChangeEvent(event, dep)
            is DepartmentDeleted -> handleDeleteEvent(event, dep)
        }
    }
    return dep
}

fun handleCreateEvent(event: DepartmentCreated): Department {
    return Department(event.name, event.departmentId)
}

fun handleNameChangeEvent(event: DepartmentNameChanged, department: Department) {
    department.name = event.name
}

fun handleDeleteEvent(event: DepartmentDeleted, department: Department) {
    department.deleted = true
}