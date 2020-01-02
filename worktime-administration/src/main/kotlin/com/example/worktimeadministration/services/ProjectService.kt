package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.project.Project
import com.example.worktimeadministration.model.project.ProjectDto
import com.example.worktimeadministration.repositories.ProjectRepository
import com.example.worktimeadministration.repositories.ProjectRepositoryImpl
import org.springframework.stereotype.Service

@Service
class ProjectService(val projectRepository: ProjectRepositoryImpl) : MappingService<Project, ProjectDto> {

    override fun mapEntityToDto(entity: Project): ProjectDto {
        return ProjectDto(
                entity.id,
                entity.name,
                entity.startDate,
                entity.endDate
        )
    }

    override fun mapDtoToEntity(dto: ProjectDto): Project {
        return Project(
                dto.id,
                dto.name,
                dto.startDate,
                dto.endDate,
                projectRepository.getById(dto.id).map { it.employees }.orElseThrow(),
                false
        )
    }
}