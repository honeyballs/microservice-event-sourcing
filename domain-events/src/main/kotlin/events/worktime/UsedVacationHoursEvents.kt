package events.worktime

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.Event

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