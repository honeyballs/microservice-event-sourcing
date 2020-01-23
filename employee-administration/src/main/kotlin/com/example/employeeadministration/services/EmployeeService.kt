package com.example.employeeadministration.services

import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.dto.EmployeeDto
import com.example.employeeadministration.repositories.department.DepartmentRepositoryGlobal
import com.example.employeeadministration.repositories.employee.EmployeeRepositoryGlobal
import com.example.employeeadministration.repositories.position.PositionRepositoryGlobal
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeService(
        val employeeRepository: EmployeeRepositoryGlobal,
        val departmentRepository: DepartmentRepositoryGlobal,
        val positionRepository: PositionRepositoryGlobal,
        val departmentService: DepartmentService,
        val positionService: PositionService,
        val eventProducer: EventProducer
): MappingService<Employee, EmployeeDto> {

    fun createEmployee(employeeDto: EmployeeDto): EmployeeDto {
        val employee = Employee.create(
                employeeDto.firstname,
                employeeDto.lastname,
                employeeDto.birthday,
                employeeDto.address,
                employeeDto.bankDetails,
                employeeDto.department.id!!,
                employeeDto.position.id!!,
                employeeDto.availableVacationHours,
                employeeDto.hourlyRate
        )
        eventProducer.produceAggregateEvent(employee)
        return mapEntityToDto(employee)
    }

    fun updateEmployee(employeeDto: EmployeeDto): EmployeeDto {
        if (employeeDto.id == null || employeeDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        val employee = employeeRepository.getByIdAndDeletedFalse(employeeDto.id).map {
            if (it.firstname != employeeDto.firstname || it.lastname != employeeDto.lastname) {
                it.changeName(employeeDto.firstname, employeeDto.lastname)
            }
            if (it.address != employeeDto.address) {
                it.moveToNewAddress(employeeDto.address)
            }
            if (it.bankDetails != employeeDto.bankDetails) {
                it.switchBankDetails(employeeDto.bankDetails)
            }
            if (it.department != employeeDto.department.id) {
                it.moveToAnotherDepartment(employeeDto.department.id!!)
            }
            if (it.position != employeeDto.position.id) {
                it.changeJobPosition(employeeDto.position.id!!)
            }
            if (it.hourlyRate != employeeDto.hourlyRate) {
                it.receiveRaiseBy(employeeDto.hourlyRate)
            }
            eventProducer.produceAggregateEvent(it)
            it
        }.orElseThrow()
        return mapEntityToDto(employee)
    }

    fun deleteEmployee(id: String) {
        employeeRepository.getByIdAndDeletedFalse(id).ifPresentOrElse({
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }) {
            throw RuntimeException("No value present")
        }
    }

    override fun mapEntityToDto(entity: Employee): EmployeeDto {
        val departmentDto = departmentRepository.getByIdAndDeletedFalse(entity.department).map { departmentService.mapEntityToDto(it) }.orElseThrow()
        val positionDto = positionRepository.getByIdAndDeletedFalse(entity.position).map { positionService.mapEntityToDto(it) }.orElseThrow()
        return EmployeeDto(
                entity.id,
                entity.firstname,
                entity.lastname,
                entity.birthday,
                entity.address,
                entity.bankDetails,
                departmentDto,
                positionDto,
                entity.hourlyRate,
                entity.availableVacationHours,
                entity.companyMail
        )
    }

    override fun mapDtoToEntity(dto: EmployeeDto): Employee {
        return Employee(
                dto.firstname,
                dto.lastname,
                dto.birthday,
                dto.address,
                dto.bankDetails,
                dto.department.id!!,
                dto.position.id!!,
                dto.availableVacationHours,
                dto.hourlyRate,
                dto.companyMail,
                // If no id was provided this is a new employee and an id is automatically generated
                dto.id ?: UUID.randomUUID().toString()
        )
    }
}