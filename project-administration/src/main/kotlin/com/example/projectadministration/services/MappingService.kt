package com.example.projectadministration.services

interface MappingService<Entity, Dto> {

    fun mapEntityToDto(entity: Entity): Dto
    fun mapDtoToEntity(dto: Dto): Entity

}