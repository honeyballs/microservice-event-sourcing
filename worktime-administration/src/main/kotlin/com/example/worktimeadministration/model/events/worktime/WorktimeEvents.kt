package com.example.worktimeadministration.model.events.worktime

import com.example.worktimeadministration.model.aggregates.EntryType
import com.example.worktimeadministration.model.events.DATE_TIME_PATTERN
import com.example.worktimeadministration.model.events.Event
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDate
import java.time.LocalDateTime

@JsonTypeName("worktime-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeCreated(
        val worktimeId: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val pause: Int,
        val projectId: String,
        val employeeId: String,
        val description: String,
        val entryType: EntryType
) : Event() {
    init {
        type = "Worktime created"
    }
}

@JsonTypeName("worktime-project-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeProjectChanged(val projectId: String) : Event() {
    init {
        type = "Worktime changed project"
    }
}

@JsonTypeName("worktime-description-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeDescriptionChanged(val description: String) : Event() {
    init {
        type = "Worktime changed description"
    }
}

@JsonTypeName("worktime-starttime-adjusted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeStarttimeAdjusted(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) val startTime: LocalDateTime) : Event() {
    init {
        type = "Worktime adjust start time"
    }
}

@JsonTypeName("worktime-endtime-adjusted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeEndtimeAdjusted(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) val endTime: LocalDateTime) : Event() {
    init {
        type = "Worktime adjust end time"
    }
}

@JsonTypeName("worktime-pause-adjusted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimePauseAdjusted(val pause: Int) : Event() {
    init {
        type = "Worktime adjust pause"
    }
}

@JsonTypeName("worktime-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeDeleted(val deleted: Boolean = true) : Event() {
    init {
        type = "Worktime deleted"
    }
}


@JsonTypeName("used-hours-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class UsedHoursCreated(val usedId: String,val employeeId: String, val hours: Int): Event() {
    init {
        type = "Used hours entry created"
    }
}

@JsonTypeName("used-hours-updated")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class UsedHoursUpdated(val hours: Int): Event() {
    init {
        type = "Used hours entry updated"
    }
}

@JsonTypeName("used-hours-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class UsedHoursDeleted(val deleted: Boolean = true): Event() {
    init {
        type = "Used hours entry deleted"
    }
}