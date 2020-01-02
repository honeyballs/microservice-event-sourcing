package com.example.worktimeadministration.model.events.worktime

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
        val description: String
) : Event() {
    init {
        type = "Worktime created"
    }
}

@JsonTypeName("worktime-time-set")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeSetTime(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) val startTime: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN) val endTime: LocalDateTime,
        val pause: Int
) : Event() {
    init {
        type = "Worktime set entry time"
    }
}

@JsonTypeName("worktime-project")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeProjectSet(val projectId: String) : Event() {
    init {
        type = "Worktime set project"
    }
}

@JsonTypeName("worktime-set-description")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class WorktimeDescriptionSet(val description: String) : Event() {
    init {
        type = "Worktime set description"
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