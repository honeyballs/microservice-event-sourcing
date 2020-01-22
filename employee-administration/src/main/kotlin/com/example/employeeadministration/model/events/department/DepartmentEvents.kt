package com.example.employeeadministration.model.events.department

import com.example.employeeadministration.model.events.Event
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("department-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class DepartmentCreated(val departmentId: String, val name: String): Event() {

    init {
        type = "Department created"
    }

}

@JsonTypeName("department-name-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class DepartmentNameChanged(val name: String): Event() {

    init {
        type = "Department name changed"
    }

}

@JsonTypeName("department-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class DepartmentDeleted(val deleted: Boolean = true): Event() {

    init {
        type = "Department deleted"
    }

}