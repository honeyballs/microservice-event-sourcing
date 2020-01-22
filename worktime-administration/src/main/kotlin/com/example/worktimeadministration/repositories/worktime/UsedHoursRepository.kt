package com.example.worktimeadministration.repositories.worktime

import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import java.util.*

interface UsedHoursRepository {

    fun getAll(): List<UsedEmployeeVacationHours>
    fun getByEmployeeIdAndDeletedFalse(employeeId: String): Optional<UsedEmployeeVacationHours>

}