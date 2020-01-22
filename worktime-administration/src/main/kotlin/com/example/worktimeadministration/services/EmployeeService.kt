package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.aggregates.employee.Employee
import com.example.worktimeadministration.model.dto.EmployeeDto
import com.example.worktimeadministration.repositories.worktime.UsedHoursRepository
import com.example.worktimeadministration.repositories.worktime.UsedHoursRepositoryGlobal
import org.springframework.stereotype.Service

@Service
class EmployeeService(
        val usedHoursRepository: UsedHoursRepositoryGlobal
): MappingService<Employee, EmployeeDto> {

    override fun mapEntityToDto(entity: Employee): EmployeeDto {
        return EmployeeDto(
                entity.id,
                entity.firstname,
                entity.lastname,
                entity.availableVacationHours,
                usedHoursRepository.getByEmployeeIdAndDeletedFalse(entity.id).map { it.hours }.orElse(0)
        )
    }

    override fun mapDtoToEntity(dto: EmployeeDto): Employee {
        TODO("Not required")
    }
}