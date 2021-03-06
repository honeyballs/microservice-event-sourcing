package events.project

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.DATE_PATTERN
import events.Event
import java.time.LocalDate

@JsonTypeName("project-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectCreated(
    val projectId: String,
    val name: String,
    val description: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val startDate: LocalDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val projectedEndDate: LocalDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val endDate: LocalDate?,
    val employees: Set<String>,
    val customer: String
) : Event() {

    init {
        type = "Project created"
    }

}

@JsonTypeName("project-description-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectDescriptionChanged(val description: String) : Event() {
    init {
        type = "Project description changed"
    }
}

@JsonTypeName("project-delayed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectDelayed( @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val projectedEndDate: LocalDate) : Event() {
    init {
        type = "Project delayed"
    }
}

@JsonTypeName("project-employee-added")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectEmployeeAdded(val employee: String) : Event() {
    init {
        type = "Project employee added"
    }
}

@JsonTypeName("project-employee-removed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectEmployeeRemoved(val employee: String) : Event() {
    init {
        type = "Project employee removed"
    }
}

@JsonTypeName("project-employees-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectEmployeesChanged(val employees: Set<String>) : Event() {
    init {
        type = "Project employees changed"
    }
}

@JsonTypeName("project-finished")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectFinished( @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN) val endDate: LocalDate) : Event() {
    init {
        type = "Project finished"
    }
}

@JsonTypeName("project-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class ProjectDeleted(val deleted: Boolean = true) : Event() {
    init {
        type = "Project deleted"
    }
}