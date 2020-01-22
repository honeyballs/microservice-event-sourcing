package com.example.employeeadministration.services

import com.example.employeeadministration.model.aggregates.Department
import com.example.employeeadministration.model.dto.DepartmentDto
import com.example.employeeadministration.repositories.department.DepartmentRepositoryGlobal
import org.springframework.stereotype.Service
import java.util.*

@Service
class DepartmentService(
        val departmentRepositoryGlobal: DepartmentRepositoryGlobal,
        val eventProducer: EventProducer
): MappingService<Department, DepartmentDto> {

    fun createDepartment(departmentDto: DepartmentDto): DepartmentDto {
        if (departmentRepositoryGlobal.getByNameAndDeletedFalse(departmentDto.name).isPresent) {
            throw RuntimeException("Department exists already")
        }
        val department = Department.create(departmentDto.name)
        eventProducer.produceAggregateEvent(department)
        return mapEntityToDto(department)
    }

    fun updateDepartment(departmentDto: DepartmentDto): DepartmentDto {
        if (departmentDto.id == null || departmentDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        return departmentRepositoryGlobal.getByIdAndDeletedFalse(departmentDto.id).map {
            if (it.name != departmentDto.name) {
                it.renameDepartment(departmentDto.name)
            }
            eventProducer.produceAggregateEvent(it)
            mapEntityToDto(it)
        }.orElseThrow()
    }

    fun deleteDepartment(id: String) {
        val department = departmentRepositoryGlobal.getByIdAndDeletedFalse(id).ifPresentOrElse({
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }) {
            throw RuntimeException("No value present")
        }
    }

    override fun mapEntityToDto(entity: Department): DepartmentDto {
        return DepartmentDto(entity.id, entity.name)
    }

    override fun mapDtoToEntity(dto: DepartmentDto): Department {
        return Department(dto.name, dto.id ?: UUID.randomUUID().toString())
    }


}