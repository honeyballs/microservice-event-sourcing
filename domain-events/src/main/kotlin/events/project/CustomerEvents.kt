package events.project

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.Event

@JsonTypeName("customer-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerCreated(
    val customerId: String,
    val name: String,
    val street: String,
    val no: Int,
    val city: String,
    val zipCode: Int,
    val firstname: String,
    val lastname: String,
    val mail: String,
    val phone: String
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
data class CustomerMoved(
    val street: String,
    val no: Int,
    val city: String,
    val zipCode: Int
) : Event() {
    init {
        type = "Customer changed location"
    }
}

@JsonTypeName("customer-contact-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class CustomerContactChanged(
    val firstname: String,
    val lastname: String,
    val mail: String,
    val phone: String
) : Event() {
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