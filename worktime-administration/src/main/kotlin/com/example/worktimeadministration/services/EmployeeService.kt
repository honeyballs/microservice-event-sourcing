package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.employee.Employee
import com.example.worktimeadministration.model.employee.EmployeeDto
import org.springframework.stereotype.Service

@Service
class EmployeeService: MappingService<Employee, EmployeeDto> {

    override fun mapEntityToDto(entity: Employee): EmployeeDto {
        return EmployeeDto(
                entity.id,
                entity.firstname,
                entity.lastname,
                entity.mail
        )
    }

    override fun mapDtoToEntity(dto: EmployeeDto): Employee {
        return Employee(
                dto.id,
                dto.firstname,
                dto.lastname,
                dto.mail,
                false
        )
    }
}