package com.example.projectadministration.model.events.customer

import com.example.projectadministration.model.aggregates.*
import com.example.projectadministration.model.events.project.*
import events.Event
import events.project.*

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
    return Customer(
            event.name,
            Address(event.street, event.no, event.city, ZipCode(event.zipCode)),
            CustomerContact(event.firstname, event.lastname, event.mail, event.phone),
            event.customerId
    )
}

fun handleNameChanged(event: CustomerNameChanged, customer: Customer) {
    customer.customerName = event.name
}

fun handleMoved(event: CustomerMoved, customer: Customer) {
    customer.address = Address(event.street, event.no, event.city, ZipCode(event.zipCode))

}

fun handleContactChanged(event: CustomerContactChanged, customer: Customer) {
    customer.contact = CustomerContact(event.firstname, event.lastname, event.mail, event.phone)

}

fun handleDeletedEvent(event: CustomerDeleted, customer: Customer) {
    customer.deleted = true
}