package com.example.worktimeadministration.model.aggregates.employee

import com.fasterxml.jackson.annotation.JsonProperty

const val EMPLOYEE_AGGREGATE = "employee"

data class Employee(
        val id: String,
        var firstname: String,
        var lastname: String,
        var availableVacationHours: Int,
        var deleted: Boolean
) {
    lateinit var companyMail: String

    // Since mail is stored in a value object which isn't relevant for this service it is reduced to a string
    @JsonProperty("companyMail")
    fun unpackMail(mail: Map<String, Any>) {
        this.companyMail = mail["mail"] as String
    }

}