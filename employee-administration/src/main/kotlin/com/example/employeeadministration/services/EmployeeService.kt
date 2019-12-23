package com.example.employeeadministration.services

import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.dto.EmployeeDto
import com.example.employeeadministration.streams.EmployeeRepositoryGlobal
import com.example.employeeadministration.streams.EmployeeRepositoryLocal
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeService(
        val employeeRepository: EmployeeRepositoryGlobal,
        val eventProducer: EventProducer
): MappingService<Employee, EmployeeDto> {

    fun createEmployee(employeeDto: EmployeeDto): EmployeeDto {
        val employee = Employee.create(
                employeeDto.firstname,
                employeeDto.lastname,
                employeeDto.address,
                employeeDto.mail, employeeDto.iban,
                employeeDto.department,
                employeeDto.title,
                employeeDto.hourlyRate
        )
        eventProducer.produceAggregateEvent(employee)
        return mapEntityToDto(employee)
    }

    fun updateEmployee(employeeDto: EmployeeDto): EmployeeDto {
        if (employeeDto.id == null) {
            throw RuntimeException("Id required to update")
        }
        val employee = employeeRepository.getById(employeeDto.id).map {
            if (it.firstname != employeeDto.firstname || it.lastname != employeeDto.lastname) {
                it.changesName(employeeDto.firstname, employeeDto.lastname)
            }
            if (it.address != employeeDto.address) {
                it.moves(employeeDto.address)
            }
            if (it.mail != employeeDto.mail) {
                it.changesMail(employeeDto.mail)
            }
            if (it.iban != employeeDto.iban) {
                it.changesBanking(employeeDto.iban)
            }
            if (it.department != employeeDto.department) {
                it.movesToDepartment(employeeDto.department)
            }
            if (it.title != employeeDto.title) {
                it.changesTitle(employeeDto.title)
            }
            if (it.hourlyRate != employeeDto.hourlyRate) {
                it.adjustRate(employeeDto.hourlyRate)
            }
            eventProducer.produceAggregateEvent(it)
            it
        }.orElseThrow()
        return mapEntityToDto(employee)
    }

    fun deleteEmployee(id: String) {
        val employee = employeeRepository.getById(id).map {
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }.orElseThrow()
    }

    override fun mapEntityToDto(entity: Employee): EmployeeDto {
        return EmployeeDto(
                entity.firstname,
                entity.lastname,
                entity.address,
                entity.mail,
                entity.iban,
                entity.department,
                entity.title,
                entity.hourlyRate,
                entity.id
        )
    }

    override fun mapDtoToEntity(dto: EmployeeDto): Employee {
        return Employee(
                dto.firstname,
                dto.lastname,
                dto.address,
                dto.mail,
                dto.iban,
                dto.department,
                dto.title,
                dto.hourlyRate,
                // If no id was provided this is a new employee and an id is automatically generated
                dto.id ?: UUID.randomUUID().toString()
        )
    }
}