package com.example.employeeadministration.repositories.position

import com.example.employeeadministration.model.aggregates.Position
import java.util.*

interface PositionRepository {

    fun getAll(): List<Position>
    fun getAllByDeletedFalse(): List<Position>
    fun getByIdAndDeletedFalse(id: String): Optional<Position>
    fun getByTitleAndDeletedFalse(title: String): Optional<Position>

}