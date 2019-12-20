package com.example.employeeadministration

import com.example.employeeadministration.model.aggregates.EMPLOYEE_AGGREGATE
import com.example.employeeadministration.model.aggregates.Employee
import com.example.employeeadministration.model.events.Event
import com.example.employeeadministration.model.events.EventType
import com.example.employeeadministration.services.EventProducer
import com.example.employeeadministration.streams.EmployeeRepository
import org.apache.kafka.streams.KafkaStreams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer
import java.math.BigDecimal

@SpringBootApplication
class EmployeeAdministrationApplication {

    @Autowired
    lateinit var producer: EventProducer

    @Autowired
    lateinit var repository: EmployeeRepository

//    @Bean
//    fun setStateListenerForTesting(): StreamsBuilderFactoryBeanCustomizer {
//        return StreamsBuilderFactoryBeanCustomizer { factoryBean ->
//            factoryBean.setStateListener { newState, oldState ->
//                run {
//                    if (newState == KafkaStreams.State.RUNNING) {
//                        val employee = Employee(
//                                "Max",
//                                "Mustermann",
//                                "Straße 1",
//                                "m.mustermann@domain.com",
//                                "128411231294814",
//                                "Java Development",
//                                "Junior Consultant",
//                                BigDecimal(20.30))
//                        println(employee.id)
//                        val event = Event(employee, EventType.CREATE)
//                        producer.sendEvent(event, EMPLOYEE_AGGREGATE)
//                        employee.firstname = "Schmax"
//                        employee.lastname = "Schmustermann"
//                        producer.sendEvent(Event(employee, EventType.UPDATE), EMPLOYEE_AGGREGATE)
//                        val employee2 = Employee(
//                                "anderer",
//                                "Mitarbeiter",
//                                "Straße 1",
//                                "m.mustermann@domain.com",
//                                "128411231294814",
//                                "Java Development",
//                                "Junior Consultant",
//                                BigDecimal(20.30))
//                        producer.sendEvent(Event(employee2, EventType.CREATE), EMPLOYEE_AGGREGATE)
//                        Thread.sleep(2000)
//                        val emp = repository.getEmployeeById(employee.id)
//                        println(emp.toString())
//                    }
//                }
//            }
//        }
//    }


}

fun main(args: Array<String>) {
    runApplication<EmployeeAdministrationApplication>(*args)
}
