package com.example.projectadministration.services

import com.example.projectadministration.model.aggregates.employee.Employee
import com.example.projectadministration.model.dto.EmployeeDto
import com.example.projectadministration.repositories.employee.DepartmentRepository
import com.example.projectadministration.repositories.employee.DepartmentRepositoryImpl
import com.example.projectadministration.repositories.employee.PositionRepository
import com.example.projectadministration.repositories.employee.PositionRepositoryImpl
import org.springframework.stereotype.Service

@Service
class EmployeeService(
        val departmentRepository: DepartmentRepositoryImpl,
        val positionRepository: PositionRepositoryImpl
): MappingService<Employee, EmployeeDto> {

    override fun mapEntityToDto(entity: Employee): EmployeeDto {
        return EmployeeDto(
                entity.id,
                entity.firstname,
                entity.lastname,
                departmentRepository.getByDepartmentId(entity.department).map { it.name }.orElseThrow(),
                positionRepository.getByPositionId(entity.position).map { it.title }.orElseThrow(),
                entity.companyMail
        )
    }

    override fun mapDtoToEntity(dto: EmployeeDto): Employee {
        TODO("not required")
    }
}