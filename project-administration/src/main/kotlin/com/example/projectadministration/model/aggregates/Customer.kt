package com.example.projectadministration.model.aggregates

import com.example.projectadministration.model.events.customer.*
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.lang.Exception
import java.util.*


const val CUSTOMER_AGGREGATE = "customer"

@JsonTypeName("customer")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
class Customer(
        var customerName: String,
        var address: Address,
        var contact: CustomerContact,
        override var id: String = UUID.randomUUID().toString(),
        var deleted: Boolean = false
): Aggregate() {

    companion object {
        fun create(name: String, address: Address, contact: CustomerContact): Customer {
            val customer = Customer(name, address, contact)
            customer.registerEvent(CustomerCreated(customer.id, name, address, contact))
            return customer
        }
    }

    init {
        aggregateName = CUSTOMER_AGGREGATE
    }

    fun changeCustomerContact(contact: CustomerContact) {
        this.contact = contact
        registerEvent(CustomerContactChanged(this.contact))
    }

    fun moveCompanyLocation(address: Address) {
        this.address = address
        registerEvent(CustomerMoved(this.address))
    }

    fun changeName(name: String) {
        this.customerName = name
        registerEvent(CustomerNameChanged(this.customerName))
    }

    fun delete() {
        this.deleted = true
        registerEvent(CustomerDeleted())
    }

    override fun toString(): String {
        return "Customer - Id: $id, Name: $customerName"
    }

}

data class CustomerContact(val firstname: String, val lastname: String, val mail: String, val phone: String)


/**
 * Wrapper class for ZipCode which checks if the provided number matches the required length.
 * Not sure if necessary for DDD or if validation suffices.
 *
 */
data class ZipCode(val zip: Int) {

    companion object {
        val ALLOWED_LENGTHS_PER_COUNTRY = hashMapOf<Locale, Int>(Pair(Locale.GERMANY, 5))
    }

    init {
        if (zip.toString().length != ALLOWED_LENGTHS_PER_COUNTRY[Locale.GERMANY]) throw Exception("The zip code provided does not match the required length of ${ALLOWED_LENGTHS_PER_COUNTRY[Locale.GERMANY]} digits.")
    }

}

/**
 * Value Object representing an address.
 */
data class Address(val street: String, val no: Int, val city: String, val zipCode: ZipCode)