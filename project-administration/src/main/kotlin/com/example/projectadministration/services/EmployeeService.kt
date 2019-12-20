package com.example.projectadministration.services

import com.example.projectadministration.model.employee.Employee
import com.example.projectadministration.model.employee.EmployeeDto
import org.springframework.stereotype.Service

@Service
class EmployeeService: MappingService<Employee, EmployeeDto> {

    override fun mapEntityToDto(entity: Employee): EmployeeDto {
        return EmployeeDto(entity.id, entity.firstname, entity.lastname, entity.mail, entity.department, entity.title)
    }

    override fun mapDtoToEntity(dto: EmployeeDto): Employee {
        return Employee(dto.id, dto.firstname, dto.lastname, dto.mail, dto.department, dto.title, false)
    }
}