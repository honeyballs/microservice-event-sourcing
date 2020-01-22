package com.example.employeeadministration.services

import com.example.employeeadministration.model.aggregates.Position
import com.example.employeeadministration.model.dto.PositionDto
import com.example.employeeadministration.repositories.position.PositionRepositoryGlobal
import org.springframework.stereotype.Service
import java.util.*

@Service
class PositionService(
        val positionRepositoryGlobal: PositionRepositoryGlobal,
        val eventProducer: EventProducer
): MappingService<Position, PositionDto> {

    fun createPosition(positionDto: PositionDto): PositionDto {
        if (positionRepositoryGlobal.getByTitleAndDeletedFalse(positionDto.title).isPresent) {
            throw RuntimeException("Position exists already")
        }
        val position = Position.create(positionDto.title, positionDto.minHourlyWage, positionDto.maxHourlyWage)
        eventProducer.produceAggregateEvent(position)
        return mapEntityToDto(position)
    }

    fun updatePosition(positionDto: PositionDto): PositionDto {
        if (positionDto.id == null || positionDto.id == "") {
            throw RuntimeException("Id required to update")
        }
        return positionRepositoryGlobal.getByIdAndDeletedFalse(positionDto.id!!).map {
            if (it.title != positionDto.title) {
                it.changePositionTitle(positionDto.title)
            }
            if (it.minHourlyWage != positionDto.minHourlyWage || it.maxHourlyWage != positionDto.maxHourlyWage) {
                it.adjustWageRange(positionDto.minHourlyWage, positionDto.maxHourlyWage)
            }
            eventProducer.produceAggregateEvent(it)
            mapEntityToDto(it)
        }.orElseThrow()
    }

    fun deletePosition(id: String) {
        val department = positionRepositoryGlobal.getByIdAndDeletedFalse(id).ifPresentOrElse({
            it.delete()
            eventProducer.produceAggregateEvent(it)
        }) {
            throw RuntimeException("No value present")
        }
    }

    override fun mapEntityToDto(entity: Position): PositionDto {
        return PositionDto(entity.id, entity.title, entity.minHourlyWage, entity.maxHourlyWage)
    }

    override fun mapDtoToEntity(dto: PositionDto): Position {
        return Position(dto.title, dto.minHourlyWage, dto.maxHourlyWage, dto.id ?: UUID.randomUUID().toString())
    }
}