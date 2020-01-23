package com.example.employeeadministration.model.events.position

import com.example.employeeadministration.model.aggregates.Position
import events.Event
import events.employee.PositionCreated
import events.employee.PositionDeleted
import events.employee.PositionTitleChanged
import events.employee.PositionWageRangeChanged


fun handlePositionEvent(event: Event, position: Position?): Position {
    var pos = position
    if (pos == null) {
        if (event is PositionCreated) {
            pos = handleCreateEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent position")
        }
    } else if (pos.deleted) {
        return pos
    } else {
        when (event) {
            is PositionTitleChanged -> handleTitleChangeEvent(event, pos)
            is PositionWageRangeChanged -> handleWageRangeChanged(event, pos)
            is PositionDeleted -> handleDeleteEvent(event, pos)
        }
    }
    return pos
}

fun handleCreateEvent(event: PositionCreated): Position {
    return Position(event.title, event.minHourlyWage, event.maxHourlyWage, event.positionId)
}

fun handleTitleChangeEvent(event: PositionTitleChanged, position: Position) {
    position.title = event.title
}

fun handleWageRangeChanged(event: PositionWageRangeChanged, position: Position) {
    position.minHourlyWage = event.minHourlyWage
    position.maxHourlyWage = event.maxHourlyWage
}

fun handleDeleteEvent(event: PositionDeleted, position: Position) {
    position.deleted = true
}