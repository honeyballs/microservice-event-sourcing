package com.example.projectadministration.repositories.customer

import com.example.projectadministration.controllers.RPC_URL
import com.example.projectadministration.model.aggregates.CUSTOMER_AGGREGATE
import com.example.projectadministration.model.aggregates.Customer
import com.example.projectadministration.model.aggregates.PROJECT_AGGREGATE
import com.example.projectadministration.model.aggregates.Project
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class CustomerRepositoryGlobal(
        val factory: StreamsBuilderFactoryBean
): CustomerRepository {

    override fun getAll(): List<Customer> {
        return getGlobalList("/customer")
    }

    override fun getAllByDeletedFalse(): List<Customer> {
        return getGlobalList("/customer/not-deleted")
    }

    override fun getByIdAndDeletedFalse(id: String): Optional<Customer> {
        return getGlobalInstance("/customer/$id", id)
    }

    /**
     * Query all instances which keep the same store.
     * Also queries itself if required, so no combining of values is necessary afterwards.
     */
    private fun getGlobalList(affix: String): List<Customer> {
        val encodedAffix = affix.replace(" ", "%20")
        return factory.kafkaStreams.allMetadataForStore("$CUSTOMER_AGGREGATE-store")
                .flatMap { metadata ->
                    val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$encodedAffix"
                    println(url)
                    RestTemplate().getForObject(url, arrayOf<Customer>()::class.java)?.toList() ?: emptyList<Customer>()
                }
    }

    private fun getGlobalInstance(affix: String, key: String): Optional<Customer> {
        val encodedAffix = affix.replace(" ", "%20")
        val metadata = factory.kafkaStreams.metadataForKey("$CUSTOMER_AGGREGATE-store", key, Serdes.String().serializer())
        val url = "http://${metadata.host()}:${metadata.port()}/$RPC_URL$encodedAffix"
        println(url)
        val customer = RestTemplate().getForObject(url, Customer::class.java)
        if (customer != null) {
            return Optional.of(customer)
        }
        return Optional.empty<Customer>()
    }

}