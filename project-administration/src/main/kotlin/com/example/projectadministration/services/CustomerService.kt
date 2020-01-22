package com.example.projectadministration.services

import com.example.projectadministration.model.aggregates.Customer
import com.example.projectadministration.model.dto.CustomerDto
import com.example.projectadministration.repositories.customer.CustomerRepository
import com.example.projectadministration.repositories.customer.CustomerRepositoryGlobal
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(
        val customerRepository: CustomerRepositoryGlobal,
        val eventProducer: EventProducer
): MappingService<Customer, CustomerDto> {

    fun createCustomer(customerDto: CustomerDto): CustomerDto {
        val customer = Customer.create(customerDto.customerName, customerDto.address, customerDto.contact)
        eventProducer.produceAggregateEvent(customer)
        return mapEntityToDto(customer)
    }

    fun updateCustomer(customerDto: CustomerDto): CustomerDto {
        if (customerDto.id == null || customerDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        return customerRepository.getByIdAndDeletedFalse(customerDto.id).map {
            if (it.customerName != customerDto.customerName) {
                it.changeName(customerDto.customerName)
            }
            if (it.address != customerDto.address) {
                it.moveCompanyLocation(customerDto.address)
            }
            if (it.contact != customerDto.contact) {
                it.changeCustomerContact(customerDto.contact)
            }
            eventProducer.produceAggregateEvent(it)
            mapEntityToDto(it)
        }.orElseThrow()
    }

    fun deleteCustomer(id: String) {
        customerRepository.getByIdAndDeletedFalse(id).ifPresentOrElse({
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }) {
            throw RuntimeException("No value present")
        }
    }

    override fun mapEntityToDto(entity: Customer): CustomerDto {
        return CustomerDto(entity.id, entity.customerName, entity.address, entity.contact)
    }

    override fun mapDtoToEntity(dto: CustomerDto): Customer {
        return Customer(dto.customerName, dto.address, dto.contact, dto.id ?: UUID.randomUUID().toString())
    }

}