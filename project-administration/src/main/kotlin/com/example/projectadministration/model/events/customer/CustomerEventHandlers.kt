package com.example.projectadministration.model.events.customer

import com.example.projectadministration.model.aggregates.Customer
import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.model.events.Event
import com.example.projectadministration.model.events.project.*

fun handleCustomerEvent(event: Event, customer: Customer?): Customer {
    var cus = customer
    if (cus == null) {
        if (event is CustomerCreated) {
            cus = handleCreateEvent(event)
        } else {
            throw RuntimeException("Trying to manipulate nonexistent customer")
        }
    } else if (cus.deleted) {
        return cus
    } else {
        when (event) {
            is CustomerNameChanged -> handleNameChanged(event, cus)
            is CustomerMoved -> handleMoved(event, cus)
            is CustomerContactChanged -> handleContactChanged(event, cus)
            is CustomerDeleted -> handleDeletedEvent(event, cus)
        }
    }
    return cus
}

fun handleCreateEvent(event: CustomerCreated): Customer {
    return Customer(event.name, event.address, event.contact, event.customerId)
}

fun handleNameChanged(event: CustomerNameChanged, customer: Customer) {
    customer.customerName = event.name
}

fun handleMoved(event: CustomerMoved, customer: Customer) {
    customer.address = event.address
}

fun handleContactChanged(event: CustomerContactChanged, customer: Customer) {
    customer.contact = event.contact
}

fun handleDeletedEvent(event: CustomerDeleted, customer: Customer) {
    customer.deleted = true
}