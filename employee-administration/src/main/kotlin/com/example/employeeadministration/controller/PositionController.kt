package com.example.employeeadministration.controller

import com.example.employeeadministration.model.dto.PositionDto
import com.example.employeeadministration.repositories.position.PositionRepositoryGlobal
import com.example.employeeadministration.services.PositionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

const val positionUrl = "position"

@RestController
class PositionController(
        val positionService: PositionService,
        val positionRepository: PositionRepositoryGlobal
) {

    @GetMapping(positionUrl)
    fun getAllPositions(): ResponseEntity<List<PositionDto>> {
        return ok(positionRepository.getAllByDeletedFalse().map { positionService.mapEntityToDto(it) })
    }

    @GetMapping("$positionUrl/{id}")
    fun getPositionById(@PathVariable("id") id: String): ResponseEntity<PositionDto> {
        return ok(positionRepository.getByIdAndDeletedFalse(id).map { positionService.mapEntityToDto(it) }.orElseThrow())

    }

    @PostMapping(positionUrl)
    fun createPosition(@RequestBody positionDto: PositionDto): ResponseEntity<PositionDto> {
        return ok(positionService.createPosition(positionDto))
    }

    @PutMapping("$positionUrl")
    fun updatePosition(@RequestBody positionDto: PositionDto): ResponseEntity<PositionDto> {
        return ok(positionService.updatePosition(positionDto))
    }

    @DeleteMapping(positionUrl)
    fun deletePosition(id: String) {
            positionService.deletePosition(id)
    }
}