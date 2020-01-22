package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.aggregates.project.Project
import com.example.worktimeadministration.model.dto.ProjectDto
import com.example.worktimeadministration.repositories.project.ProjectRepositoryImpl
import org.springframework.stereotype.Service

@Service
class ProjectService(val projectRepository: ProjectRepositoryImpl) : MappingService<Project, ProjectDto> {

    override fun mapEntityToDto(entity: Project): ProjectDto {
        return ProjectDto(
                entity.id,
                entity.name,
                entity.description,
                entity.startDate,
                entity.projectedEndDate,
                entity.endDate
        )
    }

    override fun mapDtoToEntity(dto: ProjectDto): Project {
        TODO("Not required")
    }
}