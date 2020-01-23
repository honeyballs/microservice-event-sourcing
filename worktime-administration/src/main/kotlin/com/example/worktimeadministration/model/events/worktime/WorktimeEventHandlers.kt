package com.example.worktimeadministration.model.events.worktime

import com.example.worktimeadministration.model.aggregates.EntryType
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import events.Event
import events.worktime.*

fun handleWorktimeEvent(event: Event, entry: WorktimeEntry?): WorktimeEntry {
    var e = entry
    if (e == null) {
        if (event is WorktimeCreated) {
            e = handleEntryCreatedEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent employee")
        }
    } else if (e.deleted) {
        return e
    } else {
        when (event) {
            is WorktimeProjectChanged -> handleProjectChangedEvent(event, e)
            is WorktimeDescriptionChanged -> handleDescriptionChangedEvent(event, e)
            is WorktimeStarttimeAdjusted -> handleAdjustStartTimeEvent(event, e)
            is WorktimeEndtimeAdjusted -> handleAdjustEndTimeEvent(event, e)
            is WorktimePauseAdjusted -> handleAdjustPauseEvent(event, e)
            is WorktimeDeleted -> handleDeletedEvent(event, e)
        }
    }
    return e
}

fun handleEntryCreatedEvent(event: WorktimeCreated): WorktimeEntry {
    return WorktimeEntry(
            event.startTime,
            event.endTime,
            event.pause,
            event.projectId,
            event.employeeId,
            event.description,
            EntryType.valueOf(event.entryType),
            event.worktimeId
    )
}

fun handleProjectChangedEvent(event: WorktimeProjectChanged, entry: WorktimeEntry) {
    entry.project = event.projectId
}

fun handleDescriptionChangedEvent(event: WorktimeDescriptionChanged, entry: WorktimeEntry) {
    entry.description = event.description
}

fun handleAdjustStartTimeEvent(event: WorktimeStarttimeAdjusted, entry: WorktimeEntry) {
    entry.startTime = event.startTime
}

fun handleAdjustEndTimeEvent(event: WorktimeEndtimeAdjusted, entry: WorktimeEntry) {
    entry.endTime = event.endTime
}

fun handleAdjustPauseEvent(event: WorktimePauseAdjusted, entry: WorktimeEntry) {
    entry.pauseTimeInMinutes = event.pause
}

fun handleDeletedEvent(event: WorktimeDeleted, entry: WorktimeEntry) {
    entry.deleted = true
}