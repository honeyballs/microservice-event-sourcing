package com.example.worktimeadministration.model.aggregates

import com.example.worktimeadministration.model.events.worktime.UsedHoursCreated
import com.example.worktimeadministration.model.events.worktime.UsedHoursDeleted
import com.example.worktimeadministration.model.events.worktime.UsedHoursUpdated
import java.util.*

const val VACATION_AGGREGATE = "vacation-hours"

data class UsedEmployeeVacationHours(
        var hours: Int,
        val employee: String,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    init {
        aggregateName = VACATION_AGGREGATE
    }

    companion object {
        fun create(employeeId: String, hours: Int): UsedEmployeeVacationHours {
            val usedHours = UsedEmployeeVacationHours(hours, employeeId)
            usedHours.registerEvent(UsedHoursCreated(usedHours.id, employeeId, hours))
            return usedHours
        }
    }

    fun updateHours(hours: Int) {
        this.hours = hours
        registerEvent(UsedHoursUpdated(this.hours))
    }

    fun delete() {
        this.deleted = true
        registerEvent(UsedHoursDeleted())
    }

}