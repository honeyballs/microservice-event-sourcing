package com.example.projectadministration.services

import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.model.dto.ProjectCustomerDto
import com.example.projectadministration.model.dto.ProjectDto
import com.example.projectadministration.model.dto.ProjectEmployeeDto
import com.example.projectadministration.repositories.customer.CustomerRepositoryGlobal
import com.example.projectadministration.repositories.employee.EmployeeRepositoryImpl
import com.example.projectadministration.repositories.project.ProjectRepositoryGlobal
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectService(
        val projectRepositoryGlobal: ProjectRepositoryGlobal,
        val employeeRepository: EmployeeRepositoryImpl,
        val customerRepositoryGlobal: CustomerRepositoryGlobal,
        val employeeService: EmployeeService,
        val eventProducer: EventProducer
) : MappingService<Project, ProjectDto> {

    fun createProject(projectDto: ProjectDto): ProjectDto {
        val project = Project.create(
                projectDto.name,
                projectDto.description,
                projectDto.startDate,
                projectDto.projectedEndDate,
                projectDto.endDate,
                projectDto.projectEmployees.map { it.id }.toSet(),
                projectDto.customer.id
        )
        eventProducer.produceAggregateEvent(project)
        return mapEntityToDto(project)
    }

    fun updateProject(projectDto: ProjectDto): ProjectDto {
        if (projectDto.id == null || projectDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        return projectRepositoryGlobal.getById(projectDto.id).map {
            if (it.description != projectDto.description) {
                it.updateProjectDescription(projectDto.description)
            }
            if (it.projectedEndDate != projectDto.projectedEndDate) {
                it.delayProject(projectDto.projectedEndDate)
            }
            if (it.employees != projectDto.projectEmployees.map { it.id }.toSet()) {
                it.changeEmployeesWorkingOnProject(projectDto.projectEmployees.map { it.id }.toSet())
            }
            eventProducer.produceAggregateEvent(it)
            mapEntityToDto(it)
        }.orElseThrow()
    }

    fun finishProject(projectDto: ProjectDto): ProjectDto {
        if (projectDto.id == null || projectDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        return projectRepositoryGlobal.getById(projectDto.id).map {
            if (projectDto.endDate != null) {
                it.finishProject(projectDto.endDate)
            }
            eventProducer.produceAggregateEvent(it)
            mapEntityToDto(it)
        }.orElseThrow()
    }

    fun deleteProject(id: String) {
        projectRepositoryGlobal.getById(id).map {
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }.orElseThrow()
    }

    override fun mapEntityToDto(entity: Project): ProjectDto {
        val employees = employeeRepository.getAllByIdIn(entity.employees.toList()).toSet()
        val customer = customerRepositoryGlobal.getByIdAndDeletedFalse(entity.customer).orElseThrow()
        return ProjectDto(
                entity.id,
                entity.name,
                entity.description,
                entity.startDate,
                entity.projectedEndDate,
                entity.endDate,
                employees.map { ProjectEmployeeDto(it.id, it.firstname, it.lastname, it.mail) }.toSet(),
                ProjectCustomerDto(customer.id, customer.customerName)
        )
    }

    override fun mapDtoToEntity(dto: ProjectDto): Project {
        return Project(
                dto.name,
                dto.description,
                dto.startDate,
                dto.projectedEndDate,
                dto.endDate,
                dto.projectEmployees.map { it.id }.toSet(),
                dto.customer.id,
                dto.id ?: UUID.randomUUID().toString()
        )
    }

}