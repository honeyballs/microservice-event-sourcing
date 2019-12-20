package com.example.projectadministration.model.events.project

import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.model.events.Event


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
            event.employees,
            null,
            event.projectId
    )
}

fun handleDescriptionUpdatedEvent(event: ProjectDescriptionChanged, pro: Project): Project {
    pro.description = event.description
    return pro
}

fun handleDelayedEvent(event: ProjectDelayed, pro: Project): Project {
    pro.projectedEndDate = event.projectedEndDate
    return pro
}

fun handleEmployeeAddedEvent(event: ProjectEmployeeAdded, pro: Project): Project {
    pro.employees = pro.employees.plus(event.employee)
    return pro
}

fun handleEmployeeRemovedEvent(event: ProjectEmployeeRemoved, pro: Project): Project {
    pro.employees = pro.employees.minus(event.employee)
    return pro
}

fun handleProjectFinishedEvent(event: ProjectFinished, pro: Project): Project {
    pro.endDate = event.endDate
    return pro
}

fun handleDeletedEvent(event: ProjectDeleted, pro: Project): Project {
    pro.deleted = true
    return pro
}