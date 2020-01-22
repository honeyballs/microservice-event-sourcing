package com.example.projectadministration.model.events.customer

import com.example.projectadministration.model.aggregates.Address
import com.example.projectadministration.model.aggregates.CustomerContact
import com.example.projectadministration.model.aggregates.DATE_PATTERN
import com.example.projectadministration.model.events.Event
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDate

@JsonTypeName("customer-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerCreated(
        val customerId: String,
        val name: String,
        val address: Address,
        val contact: CustomerContact
) : Event() {

    init {
        type = "Customer created"
    }

}

@JsonTypeName("customer-name-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerNameChanged(val name: String) : Event() {
    init {
        type = "Customer name changed"
    }
}

@JsonTypeName("customer-moved")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerMoved(val address: Address) : Event() {
    init {
        type = "Customer changed location"
    }
}

@JsonTypeName("customer-contact-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerContactChanged(val contact: CustomerContact) : Event() {
    init {
        type = "Customer contact changed"
    }
}

@JsonTypeName("customer-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerDeleted(val deleted: Boolean = true) : Event() {
    init {
        type = "Customer deleted"
    }
}