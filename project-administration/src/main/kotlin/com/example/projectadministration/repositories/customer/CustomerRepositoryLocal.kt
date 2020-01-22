package com.example.projectadministration.repositories.customer

import com.example.projectadministration.model.aggregates.CUSTOMER_AGGREGATE
import com.example.projectadministration.model.aggregates.Customer
import com.example.projectadministration.model.aggregates.PROJECT_AGGREGATE
import com.example.projectadministration.model.aggregates.Project
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerRepositoryLocal(
        val factory: StreamsBuilderFactoryBean
): CustomerRepository {

    override fun getAll(): List<Customer> {
        val store = factory.kafkaStreams.store("$CUSTOMER_AGGREGATE-store", QueryableStoreTypes.keyValueStore<String, Customer>())
        val iterator = store.all()
        val list = mutableListOf<Customer>()
        iterator.forEach { kv -> list.add(kv.value) }
        return list
    }

    override fun getAllByDeletedFalse(): List<Customer> {
        return getAll().filter { !it.deleted }
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Customer> {
        return Optional.ofNullable(getAllByDeletedFalse().firstOrNull { it.id == id })
    }


}