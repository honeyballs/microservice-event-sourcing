package com.example.projectadministration.controllers

import com.example.projectadministration.model.aggregates.Project
import com.example.projectadministration.repositories.ProjectRepositoryLocal
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

const val RPC_URL = "rpc"

@RestController
class KafkaRPCController(val projectRepository: ProjectRepositoryLocal) {

    @GetMapping("$RPC_URL/project")
    fun getAllProjects(): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAll())
    }

    @GetMapping("$RPC_URL/project/{key}")
    fun getProjectByKey(@PathVariable("key") key: String): ResponseEntity<Project> {
        return ok(projectRepository.getById(key).orElseThrow())
    }

    @GetMapping("$RPC_URL/project/not-deleted")
    fun getAllByDeletedFalse(): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllByDeletedFalse())
    }

    @GetMapping("$RPC_URL/project/running")
    fun getAllRunningProjects(): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllByIsRunning())
    }

    @GetMapping("$RPC_URL/project/employee/{key}")
    fun getAllOfEmployee(@PathVariable("key") key: String): ResponseEntity<List<Project>> {
        return ok(projectRepository.getAllOfEmployeeId(key))
    }

    @GetMapping("$RPC_URL/project/name/{name}")
    fun getByName(@PathVariable("name") name: String): ResponseEntity<List<Project>> {
        return ok(projectRepository.getByName(name))
    }

}