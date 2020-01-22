package com.example.projectadministration.repositories.customer

import com.example.projectadministration.model.aggregates.Customer
import java.util.*

interface CustomerRepository {

    fun getAll(): List<Customer>
    fun getAllByDeletedFalse(): List<Customer>
    fun getByIdAndDeletedFalse(id: String): Optional<Customer>

}