package com.example.worktimeadministration.services

import com.example.worktimeadministration.model.aggregates.EntryType
import com.example.worktimeadministration.model.aggregates.UsedEmployeeVacationHours
import com.example.worktimeadministration.model.aggregates.WorktimeEntry
import com.example.worktimeadministration.model.dto.WorktimeEntryDto
import com.example.worktimeadministration.repositories.employee.EmployeeRepositoryImpl
import com.example.worktimeadministration.repositories.project.ProjectRepositoryImpl
import com.example.worktimeadministration.repositories.worktime.UsedHoursRepository
import com.example.worktimeadministration.repositories.worktime.UsedHoursRepositoryGlobal
import com.example.worktimeadministration.repositories.worktime.WorktimeEntryRepositoryGlobal
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class WorktimeEntryService(
        val worktimeEntryRepository: WorktimeEntryRepositoryGlobal,
        val employeeService: EmployeeService,
        val projectService: ProjectService,
        val employeeRepository: EmployeeRepositoryImpl,
        val projectRepository: ProjectRepositoryImpl,
        val usedHoursRepository: UsedHoursRepositoryGlobal,
        val eventProducer: EventProducer
): MappingService<WorktimeEntry, WorktimeEntryDto> {


    fun createWorktimeEntry(worktimeEntryDto: WorktimeEntryDto): WorktimeEntryDto {
        if (!isEntryInProjectRange(worktimeEntryDto.startTime, worktimeEntryDto.endTime, worktimeEntryDto.project.id)) {
            throw RuntimeException("Times are not within the project range")
        }
        var usedEmployeeVacationHours: UsedEmployeeVacationHours? = null
        if (worktimeEntryDto.type == EntryType.VACATION) {
            val requestedHours = worktimeEntryDto.startTime.until(worktimeEntryDto.endTime, ChronoUnit.HOURS).toInt()
            if (!employeeHasEnoughVacationHours(requestedHours, worktimeEntryDto.employee.usedHours, worktimeEntryDto.employee.availableVacationHours)) {
                throw RuntimeException("Not enough available vacation time")
            }
            usedHoursRepository.getByEmployeeIdAndDeletedFalse(worktimeEntryDto.employee.id).ifPresentOrElse({
                it.updateHours(it.hours + requestedHours)
                usedEmployeeVacationHours = it
            }) {
                usedEmployeeVacationHours = UsedEmployeeVacationHours.create(worktimeEntryDto.employee.id, requestedHours)
            }
        }
        val entry = WorktimeEntry.create(
                worktimeEntryDto.startTime,
                worktimeEntryDto.endTime,
                worktimeEntryDto.pauseTimeInMinutes,
                worktimeEntryDto.project.id,
                worktimeEntryDto.employee.id,
                worktimeEntryDto.description,
                worktimeEntryDto.type
        )
        eventProducer.produceAggregateEvent(entry)
        if (usedEmployeeVacationHours != null) {
            eventProducer.produceAggregateEvent(usedEmployeeVacationHours!!)
        }
        return mapEntityToDto(entry)
    }

    fun updateEntry(worktimeEntryDto: WorktimeEntryDto): WorktimeEntryDto {
        if (worktimeEntryDto.id == null || worktimeEntryDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        val entry = worktimeEntryRepository.getById(worktimeEntryDto.id).map {
            var usedEmployeeVacationHours: UsedEmployeeVacationHours? = null
            if (it.startTime != worktimeEntryDto.startTime) {
                if (isEntryInProjectRange(worktimeEntryDto.startTime, worktimeEntryDto.endTime, worktimeEntryDto.project.id)) {
                    it.adjustStartTime(worktimeEntryDto.startTime)
                } else {
                    throw RuntimeException("Times are not within the project range")
                }
                if (it.type == EntryType.VACATION) {
                    if (it.startTime < worktimeEntryDto.startTime) {
                        usedEmployeeVacationHours = usedHoursRepository.getByEmployeeIdAndDeletedFalse(worktimeEntryDto.employee.id).get()
                        usedEmployeeVacationHours.updateHours(usedEmployeeVacationHours.hours - it.startTime.until(worktimeEntryDto.startTime, ChronoUnit.HOURS).toInt())
                    } else {
                        usedEmployeeVacationHours = usedHoursRepository.getByEmployeeIdAndDeletedFalse(worktimeEntryDto.employee.id).get()
                        val additionalTime = worktimeEntryDto.startTime.until(it.startTime, ChronoUnit.HOURS).toInt()
                        if (!employeeHasEnoughVacationHours(usedEmployeeVacationHours.hours + additionalTime, worktimeEntryDto.employee.usedHours, worktimeEntryDto.employee.availableVacationHours)) {
                            throw RuntimeException("Not enough available vacation time")
                        } else {
                            usedEmployeeVacationHours.updateHours(usedEmployeeVacationHours.hours + additionalTime)
                        }
                    }
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
                    it.changeProject(worktimeEntryDto.project.id)
                } else {
                    throw RuntimeException("Times are not within the project range")
                }
            }
            if (it.description != worktimeEntryDto.description) {
                it.changeDescription(worktimeEntryDto.description)
            }
            eventProducer.produceAggregateEvent(it)
            if (usedEmployeeVacationHours != null) {
                eventProducer.produceAggregateEvent(usedEmployeeVacationHours)
            }
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

    private fun employeeHasEnoughVacationHours(requestedHours: Int, used: Int, available: Int): Boolean {
        return available - used >= requestedHours
    }

    override fun mapEntityToDto(entity: WorktimeEntry): WorktimeEntryDto {
        return WorktimeEntryDto(
                entity.id,
                entity.startTime,
                entity.endTime,
                entity.pauseTimeInMinutes,
                projectRepository.getById(entity.project).map { projectService.mapEntityToDto(it) }.orElseThrow(),
                employeeRepository.getById(entity.employee).map { employeeService.mapEntityToDto(it) }.orElseThrow(),
                entity.description,
                entity.type
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
                dto.type,
                dto.id ?: UUID.randomUUID().toString()
        )
    }


}