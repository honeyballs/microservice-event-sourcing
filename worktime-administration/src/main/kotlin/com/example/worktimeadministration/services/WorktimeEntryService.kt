package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.dto.WorktimeEntryDto
import com.example.worktimeadministration.repositories.EmployeeRepositoryImpl
import com.example.worktimeadministration.repositories.ProjectRepositoryImpl
import com.example.worktimeadministration.repositories.WorktimeEntryRepositoryGlobal
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WorktimeEntryService(
        val worktimeEntryRepository: WorktimeEntryRepositoryGlobal,
        val employeeService: EmployeeService,
        val projectService: ProjectService,
        val employeeRepository: EmployeeRepositoryImpl,
        val projectRepository: ProjectRepositoryImpl,
        val eventProducer: EventProducer
): MappingService<WorktimeEntry, WorktimeEntryDto> {


    fun createWorktimeEntry(worktimeEntryDto: WorktimeEntryDto): WorktimeEntryDto {
        val entry = WorktimeEntry.create(
                worktimeEntryDto.startTime,
                worktimeEntryDto.endTime,
                worktimeEntryDto.pauseTimeInMinutes,
                worktimeEntryDto.project.id,
                worktimeEntryDto.employee.id,
                worktimeEntryDto.description
        )
        eventProducer.produceAggregateEvent(entry)
        return mapEntityToDto(entry)
    }

    fun updateEntry(worktimeEntryDto: WorktimeEntryDto): WorktimeEntryDto {
        if (worktimeEntryDto.id == null || worktimeEntryDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        val entry = worktimeEntryRepository.getById(worktimeEntryDto.id).map {
            if (it.startTime != worktimeEntryDto.startTime) {
                if (isEntryInProjectRange(worktimeEntryDto.startTime, worktimeEntryDto.endTime, worktimeEntryDto.project.id)) {
                    it.adjustStartTime(worktimeEntryDto.startTime)
                } else {
                    throw RuntimeException("Times are not within the project range")
                }
            }
            if (it.endTime != worktimeEntryDto.endTime) {
                if (isEntryInProjectRange(worktimeEntryDto.startTime, worktimeEntryDto.endTime, worktimeEntryDto.project.id)) {
                    it.adjustEndTime(worktimeEntryDto.endTime)
                } else {
                    throw RuntimeException("Times are not within the project range")
                }
            }
            if (it.pauseTimeInMinutes != worktimeEntryDto.pauseTimeInMinutes) {
                it.adjustPause(worktimeEntryDto.pauseTimeInMinutes)
            }
            if (it.project != worktimeEntryDto.project.id) {
                if (isEntryInProjectRange(worktimeEntryDto.startTime, worktimeEntryDto.endTime, worktimeEntryDto.project.id)) {
                    it.forProject(worktimeEntryDto.project.id)
                } else {
                    throw RuntimeException("Times are not within the project range")
                }
            }
            if (it.description != worktimeEntryDto.description) {
                it.enterDescription(worktimeEntryDto.description)
            }
            eventProducer.produceAggregateEvent(it)
            it
        }.orElseThrow()
        return mapEntityToDto(entry)
    }

    fun deleteEntry(id: String) {
        val entry = worktimeEntryRepository.getById(id).map {
            it.deleteEntry()
            eventProducer.produceAggregateEvent(it)
        }.orElseThrow()
    }

    /**
     * A project must not be finished and the entry times have to be after the project start date.
     * Since the WorktimeEntry aggregate only keeps an id as reference this constraint has to be evaluated in the service
     */
    private fun isEntryInProjectRange(startTime: LocalDateTime, endTime: LocalDateTime, projectId: String): Boolean {
       val project = projectRepository.getById(projectId).orElseThrow()
        return project.endDate == null && startTime.isAfter(project.startDate.atStartOfDay()) && endTime.isAfter(project.startDate.atStartOfDay())
   }

    override fun mapEntityToDto(entity: WorktimeEntry): WorktimeEntryDto {
        return WorktimeEntryDto(
                entity.id,
                entity.startTime,
                entity.endTime,
                entity.pauseTimeInMinutes,
                projectRepository.getById(entity.project).map { projectService.mapEntityToDto(it) }.orElseThrow(),
                employeeRepository.getById(entity.employee).map { employeeService.mapEntityToDto(it) }.orElseThrow(),
                entity.description
        )
    }

    override fun mapDtoToEntity(dto: WorktimeEntryDto): WorktimeEntry {
        return WorktimeEntry(
                dto.startTime,
                dto.endTime,
                dto.pauseTimeInMinutes,
                dto.project.id,
                dto.employee.id,
                dto.description,
                dto.id!!
        )
    }


}