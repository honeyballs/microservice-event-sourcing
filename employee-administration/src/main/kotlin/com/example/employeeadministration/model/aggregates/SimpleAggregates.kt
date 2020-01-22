package com.example.employeeadministration.model.aggregates

import com.example.employeeadministration.model.events.department.DepartmentCreated
import com.example.employeeadministration.model.events.department.DepartmentDeleted
import com.example.employeeadministration.model.events.department.DepartmentNameChanged
import com.example.employeeadministration.model.events.position.PositionCreated
import com.example.employeeadministration.model.events.position.PositionDeleted
import com.example.employeeadministration.model.events.position.PositionTitleChanged
import com.example.employeeadministration.model.events.position.PositionWageRangeChanged
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

const val DEPARTMENT_AGGREGATE = "department"

/**
 * Department aggregate
 */
@JsonTypeName("department")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class Department(
        var name: String,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    init {
        aggregateName = DEPARTMENT_AGGREGATE
    }

    companion object {
        fun create(name: String): Department {
            val department = Department(name)
            department.registerEvent(DepartmentCreated(department.id, department.name))
            return department
        }
    }

    fun renameDepartment(name: String) {
        this.name = name
        registerEvent(DepartmentNameChanged(this.name))
    }

    fun delete() {
        this.deleted = true
        registerEvent(DepartmentDeleted())
    }

    override fun toString(): String {
        return "Department - Id: $id, Name: $name"
    }

}

const val POSITION_AGGREGATE = "position"


/**
 * Position aggregate
 */
@JsonTypeName("position")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
class Position(
        var title: String,
        minHourlyWage: BigDecimal,
        maxHourlyWage: BigDecimal,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
) : Aggregate() {

    var minHourlyWage: BigDecimal = minHourlyWage.setScale(2, RoundingMode.HALF_UP)
        set(value) {
            field = value.setScale(2, RoundingMode.HALF_UP)
        }

    var maxHourlyWage: BigDecimal = maxHourlyWage.setScale(2, RoundingMode.HALF_UP)
        set(value) {
            field = value.setScale(2, RoundingMode.HALF_UP)
        }

    init {
        aggregateName = POSITION_AGGREGATE
    }

    companion object {
        fun create(title: String, minHourlyWage: BigDecimal, maxHourlyWage: BigDecimal): Position {
            val position = Position(title, minHourlyWage.setScale(2, RoundingMode.HALF_UP), maxHourlyWage.setScale(2, RoundingMode.HALF_UP))
            position.registerEvent(PositionCreated(position.id, position.title, position.minHourlyWage, position.maxHourlyWage))
            return position
        }
    }

    fun changePositionTitle(title: String) {
        this.title = title
        registerEvent(PositionTitleChanged(this.title))
    }

    fun adjustWageRange(min: BigDecimal?, max: BigDecimal?) {
        minHourlyWage = min ?: minHourlyWage
        maxHourlyWage = max ?: maxHourlyWage
        registerEvent(PositionWageRangeChanged(this.minHourlyWage, this.maxHourlyWage))
    }

    fun delete() {
        deleted = true
        registerEvent(PositionDeleted())
    }

    override fun toString(): String {
        return "Position - Id: $id, Title: $title"
    }
}

/**
 * Function to check whether a rate is within the limits of a job position
 */
fun Position.isRateInRange(rateToCheck: BigDecimal): Boolean {
    return rateToCheck in this.minHourlyWage..this.maxHourlyWage
}
