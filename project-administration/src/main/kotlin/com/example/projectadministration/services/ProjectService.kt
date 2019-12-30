package com.example.projectadministration.services

import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.model.dto.ProjectDto
import com.example.projectadministration.repositories.EmployeeRepository
import com.example.projectadministration.repositories.EmployeeRepositoryImpl
import com.example.projectadministration.repositories.ProjectRepositoryGlobal
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectService(
        val projectRepositoryGlobal: ProjectRepositoryGlobal,
        val employeeRepository: EmployeeRepositoryImpl,
        val employeeService: EmployeeService,
        val eventProducer: EventProducer
) : MappingService<Project, ProjectDto> {

    fun createProject(projectDto: ProjectDto): ProjectDto {
        val project = Project.create(
                projectDto.name,
                projectDto.description,
                projectDto.startDate,
                projectDto.projectedEndDate,
                projectDto.employees.map { it.id }.toSet(),
                null
        )
        eventProducer.produceAggregateEvent(project)
        return mapEntityToDto(project)
    }

    fun updateProject(projectDto: ProjectDto): ProjectDto {
        if (projectDto.id == null || projectDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        val project = projectRepositoryGlobal.getById(projectDto.id).map {
            if (it.description != projectDto.description) {
                it.updateDescription(projectDto.description)
            }
            if (it.projectedEndDate != projectDto.projectedEndDate) {
                it.delayProject(projectDto.projectedEndDate)
            }
            projectDto.employees.forEach { emp ->
                if (!it.employees.contains(emp.id)) {
                    it.addEmployeeToProject(emp.id)
                }
            }
            it.employees.forEach { emp ->
                val idSet = projectDto.employees.map { e -> e.id }.toSet()
                if (!idSet.contains(emp)) {
                    it.removeEmployeeFromProject(emp)
                }
            }
            eventProducer.produceAggregateEvent(it)
            it
        }.orElseThrow()
        return mapEntityToDto(project)
    }

    fun deleteProject(id: String) {
        val project = projectRepositoryGlobal.getById(id).map {
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }.orElseThrow()
    }

    override fun mapEntityToDto(entity: Project): ProjectDto {
        val employees = employeeRepository.getAllByIdIn(entity.employees.toList()).map { employeeService.mapEntityToDto(it) }.toSet()
        return ProjectDto(
                entity.id,
                entity.name,
                entity.description,
                entity.startDate,
                entity.projectedEndDate,
                employees,
                entity.endDate
        )
    }

    override fun mapDtoToEntity(dto: ProjectDto): Project {
        return Project(
                dto.name,
                dto.description,
                dto.startDate,
                dto.projectedEndDate,
                dto.employees.map { it.id }.toSet(),
                dto.endDate,
                dto.id ?: UUID.randomUUID().toString()
        )
    }

}