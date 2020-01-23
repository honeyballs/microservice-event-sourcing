package com.example.projectadministration.model.events.project

import com.example.projectadministration.model.aggregates.Project
import events.Event
import events.project.*


fun handleProjectEvent(event: Event, project: Project?): Project {
    var pro = project
    if (pro == null) {
        if (event is ProjectCreated) {
            pro = handleCreateEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent project")
        }
    } else if (pro.deleted) {
        return pro
    } else {
        when (event) {
            is ProjectDescriptionChanged -> handleDescriptionUpdatedEvent(event, pro)
            is ProjectDelayed -> handleDelayedEvent(event, pro)
            is ProjectEmployeeAdded -> handleEmployeeAddedEvent(event, pro)
            is ProjectEmployeeRemoved -> handleEmployeeRemovedEvent(event, pro)
            is ProjectEmployeesChanged -> handleEmployeesChangedEvent(event, pro)
            is ProjectFinished -> handleProjectFinishedEvent(event, pro)
            is ProjectDeleted -> handleDeletedEvent(event, pro)
        }
    }
    return pro
}

fun handleCreateEvent(event: ProjectCreated): Project {
    return Project(
            event.name,
            event.description,
            event.startDate,
            event.projectedEndDate,
            event.endDate,
            event.employees,
            event.customer,
            event.projectId
    )
}

fun handleDescriptionUpdatedEvent(event: ProjectDescriptionChanged, pro: Project) {
    pro.description = event.description
}

fun handleDelayedEvent(event: ProjectDelayed, pro: Project) {
    pro.projectedEndDate = event.projectedEndDate
}

fun handleEmployeeAddedEvent(event: ProjectEmployeeAdded, pro: Project) {
    pro.employees = pro.employees.plus(event.employee)
}

fun handleEmployeeRemovedEvent(event: ProjectEmployeeRemoved, pro: Project) {
    pro.employees = pro.employees.minus(event.employee)
}

fun handleEmployeesChangedEvent(event: ProjectEmployeesChanged, pro: Project) {
    pro.employees = event.employees
}

fun handleProjectFinishedEvent(event: ProjectFinished, pro: Project) {
    pro.endDate = event.endDate
}

fun handleDeletedEvent(event: ProjectDeleted, pro: Project) {
    pro.deleted = true
}