package com.example.worktimeadministration.model.events.worktime

import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.events.Event

fun handleVacationHoursEvent(event: Event, entry: UsedEmployeeVacationHours?): UsedEmployeeVacationHours {
    var used = entry
    if (used == null) {
        if (event is UsedHoursCreated) {
            used = handleVacationEntryCreatedEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent hours entry")
        }
    } else if (used.deleted) {
        return used
    } else {
        when (event) {
            is UsedHoursUpdated -> handleHoursUpdated(event, used)
            is UsedHoursDeleted -> handleDeletedEvent(event, used)
        }
    }
    return used
}

fun handleVacationEntryCreatedEvent(event: UsedHoursCreated): UsedEmployeeVacationHours {
    return UsedEmployeeVacationHours(
            event.hours,
            event.employeeId,
            event.usedId
    )
}

fun handleHoursUpdated(event: UsedHoursUpdated, entry: UsedEmployeeVacationHours) {
    entry.hours = event.hours
}

fun handleDeletedEvent(event: UsedHoursDeleted, entry: UsedEmployeeVacationHours) {
    entry.deleted = true
}