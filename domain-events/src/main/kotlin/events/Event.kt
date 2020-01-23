package events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.employee.*
import events.project.*
import events.worktime.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val TIMESTAMP_PATTERN = "dd.MM.yyyy HH:mm:ss:SSS"
const val DATE_PATTERN = "dd.MM.yyyy"
const val DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss"

@JsonTypeName("event")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(

    // EMPLOYEE EVENTS

    JsonSubTypes.Type(value = EmployeeCreated::class, name = "employee-created"),
    JsonSubTypes.Type(value = EmployeeChangedName::class, name = "employee-change-name"),
    JsonSubTypes.Type(value = EmployeeMoved::class, name = "employee-moved"),
    JsonSubTypes.Type(value = EmployeeChangedBanking::class, name = "employee-changed-banking"),
    JsonSubTypes.Type(value = EmployeeChangedDepartment::class, name = "employee-changed-department"),
    JsonSubTypes.Type(value = EmployeeRateAdjusted::class, name = "employee-adjusted-rate"),
    JsonSubTypes.Type(value = EmployeeRateAdjusted::class, name = "employee-changed-position"),
    JsonSubTypes.Type(value = EmployeeDeleted::class, name = "employee-deleted"),

    JsonSubTypes.Type(value = DepartmentCreated::class, name = "department-created"),
    JsonSubTypes.Type(value = DepartmentNameChanged::class, name = "department-name-changed"),
    JsonSubTypes.Type(value = DepartmentDeleted::class, name = "department-deleted"),

    JsonSubTypes.Type(value = PositionCreated::class, name = "position-created"),
    JsonSubTypes.Type(value = PositionTitleChanged::class, name = "position-title-changed"),
    JsonSubTypes.Type(value = PositionWageRangeChanged::class, name = "position-wage-range-changed"),
    JsonSubTypes.Type(value = PositionDeleted::class, name = "position-deleted"),

    // PROJECT EVENTS

    JsonSubTypes.Type(value = ProjectCreated::class, name = "project-created"),
    JsonSubTypes.Type(value = ProjectDescriptionChanged::class, name = "project-description-changed"),
    JsonSubTypes.Type(value = ProjectDelayed::class, name = "project-delayed"),
    JsonSubTypes.Type(value = ProjectEmployeeAdded::class, name = "project-employee-added"),
    JsonSubTypes.Type(value = ProjectEmployeeRemoved::class, name = "project-employee-removed"),
    JsonSubTypes.Type(value = ProjectEmployeesChanged::class, name = "project-employees-changed"),
    JsonSubTypes.Type(value = ProjectFinished::class, name = "project-finished"),
    JsonSubTypes.Type(value = ProjectDeleted::class, name = "project-deleted"),

    JsonSubTypes.Type(value = CustomerCreated::class, name = "customer-created"),
    JsonSubTypes.Type(value = CustomerNameChanged::class, name = "customer-name-changed"),
    JsonSubTypes.Type(value = CustomerMoved::class, name = "customer-moved"),
    JsonSubTypes.Type(value = CustomerContactChanged::class, name = "customer-contact-changed"),
    JsonSubTypes.Type(value = CustomerDeleted::class, name = "customer-deleted"),

    // WORKTIME EVENTS

    JsonSubTypes.Type(value = WorktimeCreated::class, name = "worktime-created"),
    JsonSubTypes.Type(value = WorktimeProjectChanged::class, name = "worktime-project-changed"),
    JsonSubTypes.Type(value = WorktimeDescriptionChanged::class, name = "worktime-description-changed"),
    JsonSubTypes.Type(value = WorktimeStarttimeAdjusted::class, name = "worktime-starttime-adjusted"),
    JsonSubTypes.Type(value = WorktimeEndtimeAdjusted::class, name = "worktime-endtime-adjusted"),
    JsonSubTypes.Type(value = WorktimePauseAdjusted::class, name = "worktime-pause-adjusted"),
    JsonSubTypes.Type(value = WorktimeDeleted::class, name = "worktime-deleted"),

    JsonSubTypes.Type(value = UsedHoursCreated::class, name = "used-hours-created"),
    JsonSubTypes.Type(value = UsedHoursUpdated::class, name = "used-hours-updated"),
    JsonSubTypes.Type(value = UsedHoursDeleted::class, name = "used-hours-deleted")

)
abstract class Event(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN))
) {

    open lateinit var type: String

}