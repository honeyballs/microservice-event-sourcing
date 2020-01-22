package com.example.projectadministration.model.aggregates.employee

import com.fasterxml.jackson.annotation.JsonProperty


const val EMPLOYEE_AGGREGATE = "employee"
const val DEPARTMENT_AGGREGATE = "department"
const val POSITION_AGGREGATE = "position"

data class Department(val id: String, var name: String, var deleted: Boolean)

data class Position(val id: String, var title: String, var deleted: Boolean)

data class Employee(
        val id: String,
        var firstname: String,
        var lastname: String,
        var department: String,
        var position: String,
        var deleted: Boolean
) {
    lateinit var companyMail: String

    // Since mail is stored in a value object which isn't relevant for this service it is reduced to a string
    @JsonProperty("companyMail")
    fun unpackMail(mail: Map<String, Any>) {
        this.companyMail = mail["mail"] as String
    }

}